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
import esa.commons.StringUtils;
import esa.commons.concurrencytest.ThreadFactories;
import esa.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link Appender} that provides the implementations {@link Rolling} such as {@link
 * SizedBasedRolling}, {@link TimeBasedRolling}, and {@link TimeAndSizeBasedRolling}.
 */
class RollingFileAppender implements Appender {

    private static final Logger logger = LoggerFactory.getLogger(RollingFileAppender.class);

    private static final ScheduledThreadPoolExecutor SCHEDULER =
            new ScheduledThreadPoolExecutor(1,
                    ThreadFactories.namedThreadFactory("esa-logging-scheduler#", true));
    private final Rolling rolling;
    final String fileName;
    private File file;
    private FileChannel fileChannel;
    private long pos;

    private RollingFileAppender(File file, Rolling rolling) {
        Checks.checkNotNull(file, "file name");
        createDirsIfNecessary(file);
        openFileUnchecked(file);
        this.fileName = file.getPath();
        this.rolling = rolling == null ? Rolling.NOOP : rolling;
        if (logger.isDebugEnabled()) {
            logger.debug("Prepared rolling file appender for file '{}', rolling policy '{}'",
                    file.getAbsolutePath(),
                    rolling == null ? "NOOP" : rolling.getClass().getSimpleName());
        }
    }

    static RollingFileAppender newInstance(File file, Rolling rolling) {
        if (logger.isTraceEnabled()) {
            return new TracedRollingFileAppender(file, rolling);
        } else {
            return new RollingFileAppender(file, rolling);
        }
    }

    @Override
    public void append(ByteBuffer data) {
        try {
            File rollingFile = rolling.rolloverIfNecessary(file, pos);
            if (rollingFile != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Rolling current log file '{}' to '{}'",
                            file.getAbsolutePath(), rollingFile.getAbsolutePath());
                }
                // close current file
                close();
                // rename current file to rolling file
                rename(file, rollingFile);
                // open new file
                openFileUnchecked(new File(fileName));
            }
            pos += doWrite(data);
        } catch (IOException e) {
            IOUtils.closeQuietly(fileChannel);
            throw new IllegalStateException("Error while writing file '" + fileName + "'", e);
        }
    }

    int doWrite(ByteBuffer data) throws IOException {
        return fileChannel.write(data, pos);
    }

    private void rename(File src, File target) {
        createDirsIfNecessary(target);
        if (!src.renameTo(target)) {
            throw new IllegalStateException("Failed to rename file '"
                    + src.getAbsolutePath() + "' to '" + target.getAbsolutePath() + "'");
        }
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }

    private void openFileUnchecked(File file) {
        try {
            this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
            this.pos = file.length();
            this.file = file;
        } catch (IOException e) {
            IOUtils.closeQuietly(fileChannel);
            ExceptionUtils.throwException(e);
        }
    }

    private static void createDirsIfNecessary(File file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Unexpected directory");
        }
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
            if (!parent.exists()) {
                throw new IllegalStateException("Failed to create parent directories '" + file.getAbsolutePath() + "'");
            }
        }
    }

    interface Rolling {

        Rolling NOOP = (f, pos) -> null;

        /**
         * Returns a target rolling file if it is needed to rollover current log file.
         *
         * @param current current log file
         * @param pos     write position of current log file
         *
         * @return target file for rolling
         */
        File rolloverIfNecessary(File current, long pos);

    }


    abstract static class BaseRolling implements Rolling {
        static final char DELIM = '.';
        static final File[] EMPTY = new File[0];
        final String fileName;
        final String fileExe;
        final String fileNamePrefix;
        final File parent;
        final int maxHistory;

        BaseRolling(File file, int maxHistory) {
            Checks.checkNotNull(file, "file");
            Checks.checkArg(!file.isDirectory(), "file");
            Checks.checkArg(maxHistory >= 0, "maxHistory");
            this.fileName = file.getName();
            createDirsIfNecessary(file);
            File p = file.getParentFile();
            if (p == null) {
                this.parent = file.getAbsoluteFile().getParentFile();
            } else {
                this.parent = file.getParentFile();
            }
            final int d = fileName.lastIndexOf(DELIM);
            if (d == -1) {
                this.fileNamePrefix = fileName;
                this.fileExe = null;
            } else {
                this.fileNamePrefix = fileName.substring(0, d);
                this.fileExe = fileName.substring(d);
            }
            this.maxHistory = maxHistory;
        }

        void checkHistory(int maxHistory) {
            if (maxHistory > 0) {
                File[] previous = getRollingFiles();
                if (previous.length > maxHistory) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found more than maxHistory('{}') log files of '{}', try to delete them.",
                                maxHistory, fileNamePrefix + fileName + StringUtils.emptyIfNull(fileExe));
                    }
                    removeOldest(1, 0L);
                }
            }
        }

        Future<Integer> removeOldest(int least, long timeout) {
            if (timeout > 0) {
                return SCHEDULER.schedule(() -> doRemove(least),
                        timeout, TimeUnit.NANOSECONDS);
            } else {
                return SCHEDULER.submit(() -> doRemove(least));
            }
        }

        private Integer doRemove(int least) {
            File[] rollingFiles = getRollingFiles();
            if (rollingFiles.length >= maxHistory) {
                int numToDelete = Math.max(least, rollingFiles.length - maxHistory);
                for (int i = 0; i < numToDelete; i++) {
                    File toDelete = rollingFiles[i];
                    try {
                        if (!toDelete.delete()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Failed to delete history log file '{}'",
                                        toDelete.getAbsolutePath());
                            }

                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Delete history log file '{}'",
                                        toDelete.getAbsolutePath());
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Error while deleting history log file '{}'",
                                toDelete.getAbsolutePath(), e);
                    }
                }
                return 0;
            } else {
                // maybe some of the rolling files had been deleted by some one else
                return maxHistory - rollingFiles.length;
            }
        }

        synchronized File[] getRollingFiles() {
            File[] rollingFiles = parent.listFiles(pathname -> {
                if (pathname.isDirectory()) {
                    return false;
                }
                return isRollingFile(pathname);
            });
            if (rollingFiles == null) {
                return EMPTY;
            }
            Arrays.sort(rollingFiles, comparator());
            return rollingFiles;
        }

        abstract boolean isRollingFile(File file);

        abstract Comparator<File> comparator();
    }

    static class SizedBasedRolling extends BaseRolling implements Rolling {
        private final long maxSize;
        private int nextIndex;
        private int indexLimit;
        Future<Integer> removeOldestFuture;

        SizedBasedRolling(File file, int maxHistory, long maxSize) {
            super(file, maxHistory);
            Checks.checkArg(maxSize > 0, "maxSize");
            this.maxSize = maxSize;
            checkHistory(maxHistory);
        }

        @Override
        void checkHistory(int maxHistory) {
            File[] previous = getRollingFiles();
            if (previous.length > 0) {
                this.nextIndex = extractIndex(previous[previous.length - 1], fileNamePrefix, fileExe) + 1;
            } else {
                this.nextIndex = 1;
            }
            if (maxHistory > 0) {
                this.indexLimit = Math.max(this.nextIndex + 1, this.nextIndex + maxHistory - previous.length);
                if (previous.length > maxHistory) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found more than maxHistory('{}') log files of '{}', try to delete them.",
                                maxHistory, parent.getPath() + fileName);
                    }
                    removeOldest(1, 0L);
                }
            } else {
                this.indexLimit = Integer.MAX_VALUE;
            }
        }

        @Override
        public File rolloverIfNecessary(File current, long pos) {
            if (pos < maxSize) {
                return null;
            }

            String newFile;
            if (fileExe == null) {
                newFile = fileName + DELIM + this.nextIndex;
            } else {
                newFile = fileNamePrefix + DELIM + this.nextIndex + fileExe;
            }

            if (removeOldestFuture != null && removeOldestFuture.isDone()) {
                try {
                    int idle = removeOldestFuture.get();
                    this.indexLimit += idle;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Missing {} of history log files('{}')",
                                idle, parent.getPath() + fileName);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                this.removeOldestFuture = null;
            }
            if (++nextIndex > indexLimit) {
                indexLimit++;
                removeOldestFuture = removeOldest(1, 0L);
            }
            return new File(parent, newFile);
        }

        @Override
        boolean isRollingFile(File file) {
            return extractIndex(file, fileNamePrefix, fileExe) > 0;
        }

        @Override
        Comparator<File> comparator() {
            return (o1, o2) -> {
                int order1 = extractIndex(o1, fileNamePrefix, fileExe);
                int order2 = extractIndex(o2, fileNamePrefix, fileExe);
                return Integer.compare(order1, order2);
            };
        }

        static int extractIndex(File file, String fileNamePrefix, String fileExe) {
            String name = file.getName();
            int i = name.lastIndexOf(DELIM);
            if (i > 0 && i != name.length() - 1 && name.startsWith(fileNamePrefix)) {
                if (fileExe == null) {
                    try {
                        return Integer.parseInt(name.substring(i + 1));
                    } catch (Exception ignored) {
                    }
                } else {
                    if (name.endsWith(fileExe)) {
                        int j = name.lastIndexOf(DELIM, i - 1);
                        if (j > 0) {
                            try {
                                return Integer.parseInt(name.substring(j + 1, i));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
            return -1;
        }
    }

    static class TimeBasedRolling extends BaseRolling implements Rolling {
        private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
        private static final int PERIOD_HOUR = 1;
        private static final int PERIOD_DAY = 10;
        final String datePattern;
        volatile SimpleDateFormat sdf;
        final AtomicReference<File> next = new AtomicReference<>(null);

        TimeBasedRolling(File file, int maxHistory, String datePattern) {
            super(file, maxHistory);
            Checks.checkNotEmptyArg(datePattern, "datePattern");
            this.datePattern = datePattern;
            checkHistory(maxHistory);
            int period = computePeriod(datePattern);
            long delay;
            long p;
            Date now = new Date();
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
            if (period == PERIOD_HOUR) {
                Date n = nextWholeHour(calendar, now);
                delay = n.getTime() - System.currentTimeMillis();
                p = TimeUnit.HOURS.toNanos(1L);
            } else if (period == PERIOD_DAY) {
                Date n = nextWholeDay(calendar, now);
                delay = n.getTime() - System.currentTimeMillis();
                p = TimeUnit.DAYS.toNanos(1L);
            } else {
                throw new Error("unexpected");
            }

            // plus 100 mills for avoiding deviation of system time
            delay = TimeUnit.MILLISECONDS.toNanos(delay) + TimeUnit.MILLISECONDS.toNanos(5000L);

            SCHEDULER.scheduleAtFixedRate(() -> {
                String newFile = getRollingFileNameOfCurrentPeriod();
                setNextRollingFile(newFile);
                removeOldest(0, 0L);
            }, delay, p, TimeUnit.NANOSECONDS);
        }

        void setNextRollingFile(String newFile) {
            File f = new File(parent, newFile);
            next.compareAndSet(null, f);
            if (logger.isDebugEnabled()) {
                logger.debug("Set rolling file for period to {}", f.getAbsolutePath());
            }
        }

        String getRollingFileNameOfCurrentPeriod() {
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            String suffix = sdf.format(new Date());
            String newFile;
            if (fileExe == null) {
                newFile = fileName + DELIM + suffix;
            } else {
                newFile = fileNamePrefix + DELIM + suffix + fileExe;
            }
            return newFile;
        }

        private int computePeriod(String datePattern) {

            GregorianCalendar calendar = new GregorianCalendar(GMT, Locale.getDefault());

            // set sate to 1970-01-01 00:00:00 GMT
            Date epoch = new Date(0L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            simpleDateFormat.setTimeZone(GMT);

            String r0 = simpleDateFormat.format(epoch);
            Date next;
            String r1;

            next = nextWholeHour(calendar, epoch);
            r1 = simpleDateFormat.format(next);

            if (!r0.equals(r1)) {
                return PERIOD_HOUR;
            }

            next = nextWholeDay(calendar, epoch);
            r1 = simpleDateFormat.format(next);

            if (!r0.equals(r1)) {
                return PERIOD_DAY;
            }

            throw new IllegalArgumentException("Failed to parsing rolling period of pattern '" + datePattern + "'");
        }

        private Date nextWholeDay(GregorianCalendar calendar, Date epoch) {
            calendar.setTime(epoch);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DATE, 1);
            return calendar.getTime();
        }

        private Date nextWholeHour(GregorianCalendar calendar, Date epoch) {
            calendar.setTime(epoch);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            return calendar.getTime();
        }

        @Override
        public File rolloverIfNecessary(File current, long pos) {
            File next = this.next.get();
            if (next == null) {
                return null;
            }
            this.next.set(null);
            removeOldest(0, 0L);
            return next;
        }

        @Override
        synchronized File[] getRollingFiles() {
            this.sdf = new SimpleDateFormat(datePattern);
            return super.getRollingFiles();
        }

        @Override
        boolean isRollingFile(File file) {
            return extractDate(file, sdf) != null;
        }

        @Override
        Comparator<File> comparator() {
            return (o1, o2) -> {
                Date order1 = extractDate(o1, sdf);
                Date order2 = extractDate(o2, sdf);
                return Comparator.nullsLast(Date::compareTo).compare(order1, order2);
            };
        }

        private Date extractDate(File file, SimpleDateFormat sdf) {
            String name = file.getName();
            int i = name.lastIndexOf(DELIM);
            if (i > 0 && i != name.length() - 1 && name.startsWith(fileNamePrefix)) {
                if (fileExe == null) {
                    try {
                        return sdf.parse(name.substring(i + 1));
                    } catch (Exception ignored) {
                    }
                } else {
                    if (name.endsWith(fileExe)) {
                        int j = name.lastIndexOf(DELIM, i - 1);
                        if (j > 0) {
                            try {
                                return sdf.parse(name.substring(j + 1, i));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    static class TimeAndSizeBasedRolling extends TimeBasedRolling {

        private final long maxSize;
        private int nextIndex;
        private volatile String currentPeriodFileName;

        TimeAndSizeBasedRolling(File file, int maxHistory, String pattern, long maxSize) {
            super(file, maxHistory, pattern);
            this.maxSize = maxSize;
        }

        @Override
        synchronized void checkHistory(int maxHistory) {
            File[] previous = getRollingFiles();
            if (previous.length > 0) {
                DateAndIndex dateAndIndex = extractDateAndIndex(previous[previous.length - 1],
                        sdf);
                if (dateAndIndex != null) {
                    if (sdf.format(dateAndIndex.date).equals(sdf.format(new Date()))) {
                        // is in current period
                        nextIndex = dateAndIndex.index + 1;
                    }
                }
            }

            if (maxHistory > 0) {
                if (previous.length > maxHistory) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found more than maxHistory('{}') log files of '{}', try to delete them.",
                                maxHistory, fileNamePrefix + fileName + StringUtils.emptyIfNull(fileExe));
                    }
                    removeOldest(1, 0L);
                }
            }

            this.currentPeriodFileName = getCurrentPeriodFileName(getRollingFileNameOfCurrentPeriod());
        }

        @Override
        public File rolloverIfNecessary(File current, long pos) {
            File next = super.rolloverIfNecessary(current, pos);
            if (next == null) {
                if (pos < maxSize) {
                    return null;
                } else {
                    if (nextIndex == 0) {
                        // first time to create indexed file(time based file has't been created before)
                        nextIndex = 1;
                    }
                    String newFile;
                    if (fileExe == null) {
                        newFile = currentPeriodFileName + nextIndex;
                    } else {
                        newFile = currentPeriodFileName + nextIndex + fileExe;
                    }
                    nextIndex++;
                    removeOldest(0, TimeUnit.SECONDS.toNanos(3L));
                    return new File(parent, newFile);
                }
            } else {
                nextIndex = 2;
                return next;
            }
        }

        @Override
        void setNextRollingFile(String newFile) {
            String formatted;
            if (fileExe == null) {
                formatted = newFile + DELIM + '1';
            } else {
                int d = newFile.lastIndexOf(DELIM);
                if (d != -1) {
                    formatted = newFile.substring(0, d) + DELIM + '1' + fileExe;
                } else {
                    throw new Error("Unexpected");
                }
            }
            super.setNextRollingFile(formatted);
            this.currentPeriodFileName = getCurrentPeriodFileName(newFile);
        }

        private String getCurrentPeriodFileName(String newFile) {
            if (fileExe == null) {
                return newFile + DELIM;
            } else {
                int d = newFile.lastIndexOf(DELIM);
                if (d != -1) {
                    return newFile.substring(0, d) + DELIM;
                } else {
                    throw new Error("Unexpected");
                }
            }
        }

        @Override
        boolean isRollingFile(File file) {
            return extractDateAndIndex(file, sdf) != null;
        }

        @Override
        Comparator<File> comparator() {
            return (o1, o2) -> {
                DateAndIndex order1 = extractDateAndIndex(o1, sdf);
                DateAndIndex order2 = extractDateAndIndex(o2, sdf);
                return Comparator.nullsLast(DateAndIndex::compareTo).compare(order1, order2);
            };
        }

        private DateAndIndex extractDateAndIndex(File file, SimpleDateFormat sdf) {
            // fileName.yyyyMM.index.handle
            // fileName.yyyyMM.index
            String name = file.getName();
            int i = name.lastIndexOf(DELIM);
            if (i > 0 && i != name.length() - 1 && name.startsWith(fileNamePrefix)) {
                if (fileExe == null) {
                    try {
                        int index = Integer.parseInt(name.substring(i + 1));
                        return doExtract(sdf, name, i, index);
                    } catch (Exception ignored) {
                    }
                } else {
                    if (name.endsWith(fileExe)) {
                        int j = name.lastIndexOf(DELIM, i - 1);
                        if (j > 0) {
                            try {
                                int index = Integer.parseInt(name.substring(j + 1, i));
                                return doExtract(sdf, name, j, index);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
            return null;
        }

        private DateAndIndex doExtract(SimpleDateFormat sdf, String name, int i, int index) throws ParseException {
            int j = name.lastIndexOf(DELIM, i - 1);
            if (j > 0) {
                Date date = sdf.parse(name.substring(j + 1, i));
                return new DateAndIndex(date, index);
            }
            return null;
        }

        private static class DateAndIndex implements Comparable<DateAndIndex> {
            private final Date date;
            private final int index;

            private DateAndIndex(Date date, int index) {
                this.date = date;
                this.index = index;
            }

            @Override
            public int compareTo(DateAndIndex o) {
                if (this == o) {
                    return 0;
                }
                if (o == null) {
                    return 1;
                }

                int d = this.date.compareTo(o.date);
                if (d != 0) {
                    return d;
                }
                return Integer.compare(this.index, o.index);
            }
        }
    }

    private static class TracedRollingFileAppender extends RollingFileAppender {

        private static final long UNIT = TimeUnit.SECONDS.toNanos(10L);
        private long wroteBytes;
        private long totalCost;
        private int writeTimes;
        private long maxWriteCost;
        private long minWriteCost = -1;
        private long last;
        private final Map<Integer, Integer> counter = new HashMap<>(64);

        private TracedRollingFileAppender(File file, Rolling rolling) {
            super(file, rolling);
        }

        @Override
        int doWrite(ByteBuffer data) throws IOException {
            long start = System.nanoTime();
            int wrote = super.doWrite(data);
            long cost = System.nanoTime() - start;
            totalCost += cost;
            if (cost > maxWriteCost) {
                maxWriteCost = cost;
            }
            if (minWriteCost == -1) {
                minWriteCost = cost;
            } else if (cost < minWriteCost) {
                minWriteCost = cost;
            }
            wroteBytes += wrote;
            writeTimes++;

            counter.compute(wrote, (k, v) -> {
                if (v == null) {
                    return 1;
                }
                return v + 1;
            });

            long now = System.nanoTime();
            if (now - last > UNIT) {
                logger.trace("IOPS of esa logging '{}': {}M/s, write stats: times: {}, avg cost: {} mills, " +
                                "max cost: {} mills, min cost: {} mills. distribution: {}",
                        fileName,
                        this.wroteBytes / 10485760,
                        writeTimes,
                        TimeUnit.NANOSECONDS.toMillis(totalCost / writeTimes),
                        TimeUnit.NANOSECONDS.toMillis(maxWriteCost),
                        TimeUnit.NANOSECONDS.toMillis(minWriteCost),
                        counter);
                last = now;
                wroteBytes = 0L;
                totalCost = 0L;
                writeTimes = 0;
                maxWriteCost = 0L;
                minWriteCost = -1L;
            }
            return wrote;
        }
    }

}
