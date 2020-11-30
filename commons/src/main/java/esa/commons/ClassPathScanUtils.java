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

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class ClassPathScanUtils {

    private final boolean excludeInner;
    private final boolean excludes;
    private final List<Pattern> classFilters;

    public ClassPathScanUtils() {
        this(true, true, null);
    }

    /**
     * @param excludeInner should exclude inner class
     * @param includes     {@code true} to scan the classes which meet the class filters
     * @param classFilters class filters
     */
    public ClassPathScanUtils(boolean excludeInner, boolean includes,
                              List<String> classFilters) {
        this.excludeInner = excludeInner;
        this.excludes = includes;
        if (classFilters != null && !classFilters.isEmpty()) {
            final List<Pattern> patterns = new ArrayList<>(classFilters.size());
            for (String str : classFilters) {
                patterns.add(Pattern.compile("^" + str.replace("*", ".*") + "$"));
            }
            this.classFilters = patterns;
        } else {
            this.classFilters = Collections.emptyList();
        }

    }

    /**
     * get the classes.
     *
     * @param path      base package
     * @param recursive search recursively
     *
     * @return Set
     */
    public Set<Class<?>> getPackageAllClasses(String path,
                                              boolean recursive) {
        Checks.checkNotEmptyArg(path, "path");
        final Set<Class<?>> classes = new LinkedHashSet<>();
        if (path.endsWith(".")) {
            path = path.substring(0, path.lastIndexOf('.'));
        }

        Enumeration<URL> dirs;
        try {
            dirs = ClassUtils.getClassLoader().getResources(standardPath(path));
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                if ("file".equals(url.getProtocol())) {
                    scanFromFile(classes, path, URLDecoder.decode(url.getFile(), "UTF-8"), recursive);
                } else if ("jar".equals(url.getProtocol())) {
                    scanFromJar(path, url, classes, recursive);
                }
            }
        } catch (IOException ignored) {
        }

        return classes;
    }

    private void scanFromJar(String basePackage,
                             URL url,
                             Set<Class<?>> classes,
                             boolean recursive) {
        basePackage = standardPath(basePackage);

        JarFile jar;
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof JarURLConnection) {
                jar = ((JarURLConnection) conn).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (!name.startsWith(basePackage)
                            || entry.isDirectory()
                            || (!recursive && name.lastIndexOf('/') != basePackage.length())
                            || this.excludeInner && isInnerClass(name)) {
                        continue;
                    }

                    String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
                    // filter by the class filter
                    if (filterClass(classSimpleName)) {
                        classes.add(ClassUtils.getClassLoader()
                                .loadClass(name.replace('/', '.')
                                        .substring(0, name.length() - 6)));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static String standardPath(String path) {
        return path.replace('.', '/');
    }

    /**
     * scan the classes in the file
     */
    private void scanFromFile(Set<Class<?>> classes,
                              String packageName,
                              String packagePath,
                              boolean recursive) {
        final File dir = new File(packagePath);
        if (!dir.isDirectory() || !dir.exists()) {
            return;
        }

        // filter files
        File[] dirFiles = dir.listFiles(file -> {
            if (!file.exists()) {
                return false;
            } else if (file.isDirectory()) {
                return recursive;
            } else if (excludeInner && isInnerClass(file.getName())) {
                return false;
            } else {
                return filterClass(file.getName());
            }
        });
        if (dirFiles != null) {
            for (File file : dirFiles) {
                if (file.isDirectory()) {
                    scanFromFile(classes, packageName + "." + file.getName(),
                            file.getAbsolutePath(), recursive);
                } else {
                    try {
                        classes.add(ClassUtils.getClassLoader()
                                .loadClass(packageName + '.' + file.getName()
                                        .substring(0, file.getName().length() - 6)));
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
        }
    }

    private static boolean isInnerClass(String name) {
        return name.indexOf('$') != -1;
    }

    private boolean filterClass(String className) {
        if (!className.endsWith(".class")) {
            return false;
        }
        if (this.classFilters.isEmpty()) {
            return true;
        }
        final String clz = className.substring(0, className.length() - 6);
        boolean matched = classFilters.stream().allMatch(p -> p.matcher(clz).find());
        for (Pattern p : classFilters) {
            if (p.matcher(className).find()) {
                matched = true;
                break;
            }
        }
        return (excludes && matched) || (!excludes && !matched);
    }
}
