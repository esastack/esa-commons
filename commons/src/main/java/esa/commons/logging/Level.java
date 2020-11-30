/*
 * Copyright 2020 OPPO ESA Stack Project
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
package esa.commons.logging;

/**
 * Log level
 */
public class Level {

    public static final int OFF_VAL = Integer.MAX_VALUE;
    public static final int ERROR_VAL = 1000;
    public static final int WARN_VAL = 500;
    public static final int INFO_VAL = 100;
    public static final int DEBUG_VAL = 50;
    public static final int TRACE_VAL = 20;
    public static final int ALL_VAL = Integer.MIN_VALUE;

    public static final Level OFF = new Level(OFF_VAL, "OFF");
    /**
     * special handle level of plain text
     */
    public static final Level ERROR = new Level(ERROR_VAL, "ERROR");
    public static final Level WARN = new Level(WARN_VAL, "WARN");
    public static final Level INFO = new Level(INFO_VAL, "INFO");
    public static final Level DEBUG = new Level(DEBUG_VAL, "DEBUG");
    public static final Level TRACE = new Level(TRACE_VAL, "TRACE");
    public static final Level ALL = new Level(ALL_VAL, "ALL");

    private final int val;
    private final String str;

    private Level(int val, String str) {
        this.val = val;
        this.str = str;
    }


    public int val() {
        return val;
    }

    @Override
    public String toString() {
        return str;
    }
}
