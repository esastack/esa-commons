/*
 * Copyright 2022 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
