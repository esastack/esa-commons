package esa.commons.io;

import java.nio.file.Path;

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
