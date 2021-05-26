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

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Platforms {

    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final boolean IS_LINUX = isLinux0();
    private static final boolean IS_WINDOWS = isWindows0();
    private static final int JAVA_VERSION = getJavaVersion();

    private Platforms() {
    }

    public static boolean isLinux() {
        return IS_LINUX;
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static int cpuNum() {
        return NCPU;
    }

    public static int javaVersion() {
        return JAVA_VERSION;
    }

    private static boolean isLinux0() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    private static boolean isWindows0() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static int getJavaVersion() {
        String version;
        try {
            if (System.getSecurityManager() == null) {
                version = System.getProperty("java.specification.version");
            } else {
                version = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(
                        "java.specification.version"));
            }
        } catch (Throwable throwable) {
            version = "1.7";
        }

        final String[] parts = version.split("\\.");
        int v;
        if ((v = Integer.parseInt(parts[0])) == 1) {
            return Integer.parseInt(parts[1]);
        } else {
            return v;
        }
    }

}
