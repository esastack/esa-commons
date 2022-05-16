package esa.commons.io;

import java.io.File;
import java.nio.file.WatchEvent;

class WatchEventContextImpl<T> implements WatchEventContext<T> {

    private final WatchEvent<T> event;
    private final File file;

    WatchEventContextImpl(WatchEvent<T> event, File file) {
        this.event = event;
        this.file = file;
    }

    @Override
    public WatchEvent<T> event() {
        return event;
    }

    @Override
    public File file() {
        return file;
    }

    @Override
    public String toString() {
        return "WatchEventContextImpl{" +
                "event=" + event +
                ", file=" + file +
                '}';
    }
}
