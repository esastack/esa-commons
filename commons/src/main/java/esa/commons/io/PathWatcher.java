package esa.commons.io;

import java.nio.file.Path;

/**
 * The pathWatcher can watch to files or directories. If the watched path does not exist,
 * PathWatcher will create an empty directory recursively and watch to the empty directory
 */
public interface PathWatcher {

    void start();

    void stop();

    static PathWatcherBuilder watchFile(Path file) {
        return new PathWatcherBuilder(file, false, 0);
    }

    static PathWatcherBuilder watchDir(Path dir, int maxDepth) {
        return new PathWatcherBuilder(dir, true, maxDepth);
    }
}
