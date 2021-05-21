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
package esa.commons.spi.extensionloader;


import esa.commons.spi.SpiLoader;
import esa.commons.spi.extensionloader.continueiferr.TestContinueIfErrSpi;
import esa.commons.spi.extensionloader.emptyparams.Impl1;
import esa.commons.spi.extensionloader.emptyparams.Impl2;
import esa.commons.spi.extensionloader.emptyparams.Impl3;
import esa.commons.spi.extensionloader.emptyparams.TestEmptySpi;
import esa.commons.spi.extensionloader.wrapper.TestWrapperSpi;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AllTypeGetExtensionFunctionTest {

    /* Test all kinds of functions of getActivateExtension() */

    // getActivateExtension(String group)
    @Test
    void testGetActivateExtension1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getByGroup("group1"); // impl1 & impl2
        assertEquals(2, list.size());
    }

    // getActivateExtension(Map<String, String> params)
    @Test
    void testGetActivateExtension2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", "testValue2");  // impl1 & impl2
        List list = loader.getByTags(map); // impl1 & impl2
        assertEquals(2, list.size());
    }

    @Test
    void testGetAllActivateExtension() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getAll(); // impl1 & impl2
        assertEquals(5, list.size());
    }

    @Test
    void testIncludeEmptyGroup() {
        SpiLoader loader = SpiLoader.cached(TestEmptySpi.class);
        Map<String, String> map = new HashMap<>(1);
        map.put("key1", "value1");
        String testGroup = "test_group";
        List list;

        /* Only test group  */
        // includeEmptyGroup is true
        list = loader.getByFeature(testGroup, true, null, false);
        assertTrue(list.size() == 4 && list.get(0) instanceof Impl3);
        // includeEmptyGroup is false
        list = loader.getByFeature(testGroup, false, null, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl1);

        /* Only test params  */
        // includeEmptyGroup is true
        list = loader.getByFeature(null, true, map, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl2);
        // includeEmptyGroup is false
        list = loader.getByFeature(null, false, map, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl2);


        /* Test both group and params */
        // includeEmptyGroup is true
        list = loader.getByFeature(testGroup, true, map, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl2);
        // includeEmptyGroup is false
        list = loader.getByFeature(testGroup, false, map, false);
        assertEquals(0, list.size());

    }

    // getActivateExtension(String group, Map<String, String> params, boolean includeEmptyGroup, boolean
    // includeEmptyParam)
    @Test
    void testIncludeEmptyParam() {
        SpiLoader loader = SpiLoader.cached(TestEmptySpi.class);
        Map<String, String> map = new HashMap<>(1);
        map.put("key1", "value1");
        String testGroup = "test_group";
        List list;

        /* Only test group  */
        // includeEmptyParam is true
        list = loader.getByFeature(testGroup, false, null, true);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl1);
        // includeEmptyParam is false
        list = loader.getByFeature(testGroup, false, null, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl1);

        /* Only test params  */
        // includeEmptyParam is true
        list = loader.getByFeature(null, false, map, true);
        assertTrue(list.size() == 4 && list.get(0) instanceof Impl3);
        // includeEmptyParam is false
        list = loader.getByFeature(null, false, map, false);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl2);


        /* Test both group and params */
        // includeEmptyParam is true
        list = loader.getByFeature(testGroup, false, map, true);
        assertTrue(list.size() == 1 && list.get(0) instanceof Impl1);
        // includeEmptyParam is false
        list = loader.getByFeature(testGroup, false, map, false);
        assertEquals(0, list.size());

    }

    @Test
    void testOtherTypesFunction() {
        SpiLoader loader = SpiLoader.cached(TestEmptySpi.class);
        Map<String, String> map = new HashMap<>(1);
        map.put("key1", "value1");
        String testGroup = "test_group";
        List list;

        // getActivateExtension(String group, Map<String, String> params)
        list = loader.getByFeature(testGroup, map);
        assertEquals(0, list.size());

        // getActivateExtension(String group, boolean includeEmptyGroup)
        list = loader.getByGroup(testGroup, true);
        assertEquals(4, list.size());
        list = loader.getByGroup(testGroup, false);
        assertEquals(1, list.size());

        // getActivateExtension(String group)
        list = loader.getByGroup(testGroup);
        assertEquals(1, list.size());

        // getActivateExtension(Map<String, String> params, boolean includeEmptyParam)
        list = loader.getByTags(map, true);
        assertEquals(4, list.size());
        list = loader.getByTags(map, false);
        assertEquals(1, list.size());

        // getActivateExtension(Map<String, String> params)
        list = loader.getByTags(map);
        assertEquals(1, list.size());

        list = loader.getAll();
        assertEquals(4, list.size());

        TestEmptySpi impl1 = SpiLoader.getDefault(TestEmptySpi.class).orElse(null);
        assertTrue(impl1 instanceof Impl1);

        TestEmptySpi impl2 = SpiLoader.getByName(TestEmptySpi.class, "esa.commons.spi.extensionloader.emptyparams" +
                ".Impl2").orElse(null);
        assertTrue(impl2 instanceof Impl2);

        list = SpiLoader.getAll(TestEmptySpi.class);
        assertEquals(4, list.size());
    }

    @Test
    public void continueIfErrTest() {
        final SpiLoader<TestContinueIfErrSpi> loader = SpiLoader.cached(TestContinueIfErrSpi.class);
        final Map<String, String> tags = new HashMap<>();
        tags.put("k1", "v1");
        tags.put("k2", "v2");

        List<TestContinueIfErrSpi> list = null;

        try {
            list = loader.getAll();
        } catch (Exception e) {
            list = null;
        }
        assertNull(list);

        try {
            list = loader.getAll(true);
        } catch (Exception e) {
            list = null;
        }
        assertNotNull(list);
        assertEquals(2, list.size());

        try {
            list = loader.getByGroup("TEST");
        } catch (Exception e) {
            list = null;
        }
        assertNull(list);

        try {
            list = loader.getByGroup("TEST", false, true);
        } catch (Exception e) {
            list = null;
        }
        assertNotNull(list);
        assertEquals(2, list.size());

        try {
            list = loader.getByTags(tags);
        } catch (Exception e) {
            list = null;
        }
        assertNull(list);

        try {
            list = loader.getByTags(tags, false, true);
        } catch (Exception e) {
            list = null;
        }
        assertNotNull(list);
        assertEquals(2, list.size());

        try {
            list = loader.getByFeature("TEST", tags);
        } catch (Exception e) {
            list = null;
        }
        assertNull(list);

        try {
            list = loader.getByFeature("TEST", tags, true);
        } catch (Exception e) {
            list = null;
        }
        assertNotNull(list);
        assertEquals(2, list.size());
    }
}
