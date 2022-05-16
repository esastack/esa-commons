package esa.commons.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public interface WatchEventContext<T> {
    WatchEvent<T> event();

    Path path();

    boolean isDir();
}
