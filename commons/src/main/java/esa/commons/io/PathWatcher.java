package esa.commons.io;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * The pathWatcher can watch to files or directories. If the watched path does not exist,
 * PathWatcher will create an empty directory recursively and watch to the empty directory
 */
public interface PathWatcher {

    //TODO 增加注释
    void start();

    //TODO 增加注释
    boolean stopAndWait(long timeout, TimeUnit unit) throws InterruptedException;

    //TODO 增加注释
    static PathWatcherBuilder watchFile(Path file) {
        return new PathWatcherBuilder(file, false, 0);
    }

    //TODO 增加注释
    static PathWatcherBuilder watchDir(Path dir, int maxDepth) {
        return new PathWatcherBuilder(dir, true, maxDepth);
    }
}
