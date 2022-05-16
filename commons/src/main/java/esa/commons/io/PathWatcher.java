package esa.commons.io;

import java.nio.file.Path;

public interface PathWatcher {

    void start();

    void stop();

    static PathWatcherBuilder watchFile(Path file) {
        return new PathWatcherBuilder(file, false);
    }

    static PathWatcherBuilder watchDir(Path dir) {
        return new PathWatcherBuilder(dir, true);
    }
}
