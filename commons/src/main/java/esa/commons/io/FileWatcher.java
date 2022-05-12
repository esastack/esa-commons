package esa.commons.io;

import java.io.File;

public interface FileWatcher {

    void start();

    void stop();

    static FileWatcherBuilder create(File file) {
        return new FileWatcherBuilder(file);
    }
}
