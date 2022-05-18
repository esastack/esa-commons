package esa.commons.io;

import java.io.File;
import java.nio.file.WatchEvent;

//TODO 增加注释
public interface WatchEventContext<T> {
    WatchEvent<T> event();

    File file();
}
