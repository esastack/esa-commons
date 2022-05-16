package esa.commons.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

class WatchEventContextImpl<T> implements WatchEventContext<T> {

    private final WatchEvent<T> event;
    private final Path path;
    private final boolean isDir;

    WatchEventContextImpl(WatchEvent<T> event, Path path, boolean isDir) {
        this.event = event;
        this.path = path;
        this.isDir = isDir;
    }

    @Override
    public WatchEvent<T> event() {
        return event;
    }

    @Override
    public Path path() {
        return path;
    }

    @Override
    public boolean isDir() {
        return isDir;
    }

    @Override
    public String toString() {
        return "WatchEventContextImpl{" +
                "event=" + event +
                ", path=" + path +
                ", isDir=" + isDir +
                '}';
    }
}
