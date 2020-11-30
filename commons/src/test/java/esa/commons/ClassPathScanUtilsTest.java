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

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ClassPathScanUtilsTest {

    @Test
    void testScanCurrentProject() {
        final ClassPathScanUtils scanUtils = new ClassPathScanUtils();
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("esa.commons", false);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(ClassPathScanUtils.class));
        assertTrue(classes.contains(ClassPathScanUtilsTest.class));
        assertFalse(classes.contains(Inner.class));
    }

    @Test
    void testScanCurrentProjectWithExcludesPredicates() {
        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, false,
                        Collections.singletonList("ClassPathScanUtils"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("esa.commons", false);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(ClassPathScanUtilsTest.class));
        assertFalse(classes.contains(Inner.class));
        assertFalse(classes.contains(ClassPathScanUtils.class));
    }

    @Test
    void testScanCurrentProjectWithWildcardExcludesPredicates() {
        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(false, false,
                        Collections.singletonList("ClassPathScanUtils*"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("esa.commons", false);
        assertFalse(classes.isEmpty());
        assertFalse(classes.contains(ClassPathScanUtilsTest.class));
        assertFalse(classes.contains(Inner.class));
        assertFalse(classes.contains(ClassPathScanUtils.class));
    }

    @Test
    void testScanCurrentProjectIncludesPredicates() {
        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, true,
                        Collections.singletonList("ClassPathScanUtils"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("esa.commons", false);
        assertFalse(classes.isEmpty());
        assertEquals(1, classes.size());
        assertTrue(classes.contains(ClassPathScanUtils.class));
        assertFalse(classes.contains(ClassPathScanUtilsTest.class));
        assertFalse(classes.contains(Inner.class));
    }

    @Test
    void testScanCurrentProjectWithWildcardIncludesPredicates() {
        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, true,
                        Collections.singletonList("ClassPathScanUtils*"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("esa.commons", false);
        assertFalse(classes.isEmpty());
        assertEquals(2, classes.size());
        assertTrue(classes.contains(ClassPathScanUtils.class));
        assertTrue(classes.contains(ClassPathScanUtilsTest.class));

        assertFalse(classes.contains(Inner.class));
    }

    @Test
    void testScanFromJar() {
        // use slf4j as target
        assumeTrue(ClassUtils.hasClass("org.slf4j.Logger")
                && ClassUtils.hasClass("org.slf4j.LoggerFactory"));
        final ClassPathScanUtils scanUtils = new ClassPathScanUtils();
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("org.slf4j", false);

        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(org.slf4j.Logger.class));
        assertTrue(classes.contains(org.slf4j.LoggerFactory.class));
    }

    @Test
    void testScanFromJarWithExcludesPredicates() {
        // use slf4j as target
        assumeTrue(ClassUtils.hasClass("org.slf4j.Logger")
                && ClassUtils.hasClass("org.slf4j.LoggerFactory"));

        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, false,
                        Collections.singletonList("LoggerFactory"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("org.slf4j", false);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(org.slf4j.Logger.class));
        assertFalse(classes.contains(org.slf4j.LoggerFactory.class));
    }

    @Test
    void testScanFromJarWithWildcardExcludesPredicates() {
        // use slf4j as target
        assumeTrue(ClassUtils.hasClass("org.slf4j.Logger")
                && ClassUtils.hasClass("org.slf4j.LoggerFactory"));

        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, false,
                        Collections.singletonList("Logg*"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("org.slf4j", false);
        assertFalse(classes.isEmpty());
        assertFalse(classes.contains(org.slf4j.Logger.class));
        assertFalse(classes.contains(org.slf4j.LoggerFactory.class));
    }

    @Test
    void testScanFromJarWithIncludesPredicates() {
        // use slf4j as target
        assumeTrue(ClassUtils.hasClass("org.slf4j.Logger")
                && ClassUtils.hasClass("org.slf4j.LoggerFactory"));

        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, true,
                        Collections.singletonList("LoggerFactory"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("org.slf4j", false);
        assertEquals(1, classes.size());
        assertTrue(classes.contains(org.slf4j.LoggerFactory.class));
        assertFalse(classes.contains(org.slf4j.Logger.class));
    }

    @Test
    void testScanFromJarWithWildcardIncludesPredicates() {
        // use slf4j as target
        assumeTrue(ClassUtils.hasClass("org.slf4j.Logger")
                && ClassUtils.hasClass("org.slf4j.LoggerFactory"));

        final ClassPathScanUtils scanUtils =
                new ClassPathScanUtils(true, true,
                        Collections.singletonList("Logg*"));
        Set<Class<?>> classes = scanUtils.getPackageAllClasses("org.slf4j", false);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(org.slf4j.LoggerFactory.class));
        assertTrue(classes.contains(org.slf4j.Logger.class));
    }

    private static class Inner {
    }
}
