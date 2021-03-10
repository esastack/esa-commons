package esa.commons;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class NoExceptionUtilsTest {

    @Test
    void testSafeWarnLog() {
        final Logger logger = mock(Logger.class);
        doThrow(new IllegalStateException()).when(logger).warn(any(String.class), any(Throwable.class));
        assertDoesNotThrow(() -> NoExceptionUtils.WARN(logger, "foo", new IllegalArgumentException()));
    }

    @Test
    void testSafeInfoLog() {
        final Logger logger = mock(Logger.class);
        doThrow(new IllegalStateException()).when(logger).info(any(String.class), any(Throwable.class));
        assertDoesNotThrow(() -> NoExceptionUtils.INFO(logger, "foo", new IllegalArgumentException()));
    }

    @Test
    void testSafeErrorLog() {
        final Logger logger = mock(Logger.class);
        doThrow(new IllegalStateException()).when(logger).error(any(String.class), any(Throwable.class));
        assertDoesNotThrow(() -> NoExceptionUtils.ERROR(logger, "foo", new IllegalArgumentException()));
    }

    @Test
    void testSafeSleep() throws InterruptedException {

        final CountDownLatch startLatch = new CountDownLatch(1);
        final AtomicReference<Throwable> err = new AtomicReference<>();
        final Thread t = new Thread(() -> {
            startLatch.countDown();
            try {
                NoExceptionUtils.SLEEP(10);
            } catch (Throwable th) {
                err.set(th);
            }
        }, "sleep-test");
        t.start();
        t.interrupt();
        t.join();
        assertNull(err.get());
    }

}
