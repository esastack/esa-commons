/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.commons.logging;

import esa.commons.Checks;
import esa.commons.ExceptionUtils;
import esa.commons.MathUtils;
import esa.commons.Platforms;
import esa.commons.StringUtils;
import esa.commons.concurrencytest.Buffer;
import esa.commons.concurrencytest.MpscArrayBuffer;
import esa.commons.concurrencytest.UnsafeUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Encodes the given {@link LogEvent} to byte array and queues to append to the {@link Appender}, the appending will
 * be run in a single {@link Thread} and prefers to queue the encoded byte array data until the
 * {@link BaseWorker#OS_PAGE} for high IOPS.
 */
class SingleThreadLogHandler implements LogHandler {

    private static final Logger logger = LoggerFactory.getLogger(SingleThreadLogHandler.class);
    private static final int DEFAULT_QUEUE_SIZE = 16384;
    private final Consumer<byte[]> buf;
    private final Encoder encoder;
    final BaseWorker worker;

    SingleThreadLogHandler(Appender appender,
                           Encoder encoder,
                           int queueSize,
                           int writeBuffer) {
        Checks.checkNotNull(encoder);
        Checks.checkNotNull(appender);
        this.encoder = encoder;
        if (queueSize <= 0) {
            queueSize = DEFAULT_QUEUE_SIZE;
        }
        if (UnsafeUtils.hasUnsafe()) {
            queueSize = Math.max(MathUtils.nextPowerOfTwo(queueSize / Platforms.cpuNum() * 2),
                    Math.min(queueSize, 1024));
            final Buffer<byte[]> q = new MpscArrayBuffer<>(queueSize);
            this.buf = q::offer;
            this.worker = new FastWorker(appender, writeBuffer, q);
        } else {
            BlockingQueue<byte[]> q = new ArrayBlockingQueue<>(queueSize);
            this.buf = q::offer;
            this.worker = new SlowWorker(appender, writeBuffer, q);
        }
        this.worker.start();
    }

    @Override
    public void handle(LogEvent event) {
        buf.accept(encoder.encode(event));
    }

    @Override
    public void stop() {
        worker.shutdown();
        encoder.stop();
    }

    abstract static class BaseWorker extends Thread {
        private static final int OS_PAGE = 4 * 1024;
        private static final int DEFAULT_WRITE_BUFFER_SIZE;
        static final int MAX_SPIN = Math.min(Platforms.cpuNum() << 2, 64);
        static final long BUFFER_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(1000L);
        private static final AtomicInteger ID = new AtomicInteger(0);
        private static final Consumer<ByteBuffer> CLEANER;
        private final Appender appender;
        final ByteBuffer buffer;
        volatile boolean running = true;

        private BaseWorker(Appender appender,
                           int bufferSize) {
            this.appender = appender;
            if (bufferSize <= 0) {
                bufferSize = DEFAULT_WRITE_BUFFER_SIZE;
            }
            this.buffer = ByteBuffer.allocateDirect(MathUtils.nextPowerOfTwo(bufferSize));
            setName("esa-logging-appender#" + ID.getAndIncrement());
            try {
                setDaemon(true);
            } catch (Throwable ignored) {
            }
        }

        static {
            Consumer<ByteBuffer> cleaner0 = null;
            try {
                if (Platforms.javaVersion() < 9) {
                    ByteBuffer direct = ByteBuffer.allocateDirect(1);
                    Field f = direct.getClass().getDeclaredField("cleaner");
                    f.setAccessible(true);
                    Object cleaner = f.get(direct);
                    Method m = cleaner.getClass().getDeclaredMethod("clean");
                    m.invoke(cleaner);

                    cleaner0 = buf -> {
                        if (buf.isDirect()) {
                            try {
                                Object c = f.get(buf);
                                if (c != null) {
                                    m.invoke(c);
                                }
                            } catch (Throwable t) {
                                ExceptionUtils.throwException(t);
                            }
                        }
                    };
                } else if (UnsafeUtils.hasUnsafe()) {
                    ByteBuffer direct = ByteBuffer.allocateDirect(1);
                    Method m = UnsafeUtils.getUnsafe()
                            .getClass()
                            .getDeclaredMethod("invokeCleaner", ByteBuffer.class);
                    m.invoke(UnsafeUtils.getUnsafe(), direct);

                    cleaner0 = buf -> {
                        if (buf.isDirect()) {
                            try {
                                m.invoke(UnsafeUtils.getUnsafe(), buf);
                            } catch (Throwable t) {
                                ExceptionUtils.throwException(t);
                            }
                        }
                    };
                }
            } catch (Throwable t) {
                logger.error("could not find cleaner for DirectBuffer", t);
            }

            if (cleaner0 == null) {
                CLEANER = buf -> {
                };
            } else {
                CLEANER = cleaner0;
            }

            int defaultWriteBufferSize = -1;
            try {
                String prop = System.getProperty("esa.logging.defaultWriteBufferSize");
                if (!StringUtils.isEmpty(prop)) {
                    defaultWriteBufferSize = Integer.parseInt(prop);
                }
            } catch (Exception ignored) {
            }
            if (defaultWriteBufferSize <= 0) {
                defaultWriteBufferSize = OS_PAGE;
            }
            DEFAULT_WRITE_BUFFER_SIZE = defaultWriteBufferSize;
        }

        @Override
        public void run() {
            try {
                doOnLoop();
            } finally {
                shutdown();
                // already shutdown
                freeBuffer();
                try {
                    appender.close();
                } catch (IOException ignored) {
                }
            }
        }

        private void freeBuffer() {
            if (buffer != null) {
                CLEANER.accept(buffer);
            }
        }

        abstract void doOnLoop();

        void shutdown() {
            running = false;
        }

        void append(byte[] bytes) {
            final ByteBuffer buffer = this.buffer;
            int left = bytes.length;
            int offset = 0;
            int remain;
            do {
                remain = buffer.remaining();
                if (left < remain) {
                    buffer.put(bytes, offset, left);
                    break;
                } else if (left == remain) {
                    buffer.put(bytes, offset, left);
                    buffer.flip();
                    appender.append(buffer);
                    buffer.clear();
                    break;
                } else {
                    buffer.put(bytes, offset, remain);
                    buffer.flip();
                    appender.append(buffer);
                    buffer.clear();
                    offset += remain;
                    left -= remain;
                }
            } while (left > 0);
        }

        void appendNow() {
            buffer.flip();
            appender.append(buffer);
            buffer.clear();
        }
    }

    private static class FastWorker extends BaseWorker {
        final Buffer<byte[]> q;

        FastWorker(Appender appender,
                   int bufferSize,
                   Buffer<byte[]> q) {
            super(appender, bufferSize);
            this.q = q;
        }

        @Override
        void doOnLoop() {
            int leftSpins = MAX_SPIN;
            long timeout = -1L;
            boolean miss;
            while (running) {
                try {
                    if (miss = q.drain(this::append) == 0) {
                        // busy spin
                        while (leftSpins > 0) {
                            leftSpins--;
                            if (q.drain(this::append) > 0) {
                                miss = false;
                                break;
                            }
                        }
                    }
                    if (miss) {
                        // sleep for a while
                        Thread.sleep(1L);
                        // write if timeout
                        if (timeout < 0) {
                            timeout = System.nanoTime() + BUFFER_TIMEOUT;
                        } else if (buffer.position() > 0 && timeout < System.nanoTime()) {
                            appendNow();
                            timeout = -1L;
                        }
                        // continue
                    } else {
                        // reset spins
                        timeout = -1L;
                        leftSpins = MAX_SPIN;
                    }
                } catch (Throwable t) {
                    if (!(t instanceof InterruptedException)) {
                        logger.error("Logging handler is about to stopping because of unexpected error", t);
                        break;
                    } else {
                        logger.warn("Unexpected interruption of logging appender thread.");
                    }
                }
            }
        }
    }

    private static class SlowWorker extends BaseWorker {

        private final BlockingQueue<byte[]> queue;

        SlowWorker(Appender appender,
                   int bufferSize,
                   BlockingQueue<byte[]> queue) {
            super(appender, bufferSize);
            this.queue = queue;
        }

        @Override
        void doOnLoop() {
            long timeout = -1L;
            while (running) {
                try {
                    byte[] o;

                    if (buffer.position() > 0) {
                        o = queue.poll(BUFFER_TIMEOUT, TimeUnit.NANOSECONDS);
                        if (o == null) {
                            // append directly
                            appendNow();
                            continue;
                        } else {
                            // polled one, but maybe timeout
                            if (timeout < 0) {
                                timeout = System.nanoTime() + BUFFER_TIMEOUT;
                            } else if (buffer.position() > 0 && timeout < System.nanoTime()) {
                                appendNow();
                                timeout = -1L;
                                continue;
                            }
                        }
                    } else {
                        o = queue.take();
                    }
                    append(o);
                } catch (Throwable t) {
                    if (!(t instanceof InterruptedException)) {
                        logger.error("Logging handler is about to stopping because of unexpected error", t);
                        break;
                    } else {
                        logger.warn("Unexpected interruption of logging appender thread.");
                    }
                }
            }
        }
    }
}
