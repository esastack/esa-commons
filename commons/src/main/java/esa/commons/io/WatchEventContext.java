package esa.commons.io;

import java.io.File;
import java.nio.file.WatchEvent;

public interface WatchEventContext<T> {
    WatchEvent<T> event();

    File file();
}
