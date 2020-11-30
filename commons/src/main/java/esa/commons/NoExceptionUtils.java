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
package esa.commons;

import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @deprecated please do not use it
 */
@Deprecated
@SuppressWarnings("all")
public final class NoExceptionUtils {

    public static void WARN(Logger lgoeger, String msg, Throwable t) {
        try {
            lgoeger.warn(msg, t);
        } catch (Throwable throwable) {
            //do nothing
        }
    }

    public static void INFO(Logger lgoeger, String msg, Throwable t) {
        try {
            lgoeger.info(msg, t);
        } catch (Throwable throwable) {
            //do nothing
        }
    }

    public static void ERROR(Logger lgoeger, String msg, Throwable t) {
        try {
            lgoeger.error(msg, t);
        } catch (Throwable throwable) {
            //do nothing
        }
    }

    public static void SLEEP(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (Throwable e) {
            //do nothing
        }
    }

    private NoExceptionUtils() {
    }
}
