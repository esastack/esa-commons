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
import esa.commons.spi.extensionloader.excludeparam.TestExcludeParamSpi;
import esa.commons.spi.extensionloader.sort.feature.FeatureSortSpiImpl1;
import esa.commons.spi.extensionloader.sort.feature.FeatureSortSpiImpl2;
import esa.commons.spi.extensionloader.sort.feature.FeatureSortSpiImpl3;
import esa.commons.spi.extensionloader.sort.feature.TestFeatureSortSpi;
import esa.commons.spi.extensionloader.wrapper.TestWrapperSpi;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains test cases of getActivateExtension() 1) Test cases of input params: "group","key" and "value" 2)
 * Test cases of "excludeParams" of @Feature 3) Test cases of "order" of @Feature
 */
class GetFeatureExtensionTest {

    /**
     * 1) Test cases of input params: "group","key" and "value"
     */
    @Test
    void testGroupNullAndParamsNull() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getByFeature(null, new HashMap<>());
        assertEquals(5, list.size());  // impl1 & imp2 & impl3 & duplicate impl3 (impl5 doesn't have @Feature)
    }

    @Test
    void testGroup1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        List list = loader.getByFeature("group1", map); // impl1 & impl2
        assertEquals(2, list.size());
    }

    @Test
    void testGroup2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getByFeature("group2", null); // impl2 & impl3 $ Duplicate impl3
        assertEquals(3, list.size());
    }

    @Test
    void testGroup3() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getByFeature("group3", null); // no impl is in group3
        assertEquals(0, list.size());
    }

    // Key is given and is expected
    // Value is NULL
    @Test
    void testKey() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null);
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size());  // impl1 & impl2
    }

    // Value is given and is expected
    // Key is NULL
    @Test
    void testValue() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put(null, "testValue3");
        List list = loader.getByFeature(null, map);

        // A empty or a null key can NOT match
        assertEquals(0, list.size());
    }

    // "params" of @Feature only has key
    // No ":" assign
    @Test
    void testActiveWithoutValue1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey4", null);  // @Feature(params = {"testKey4"})
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size()); // impl3 & duplicate impl3
    }

    // "params" of @Feature only has key
    // Has ":" assign
    @Test
    void testActiveWithoutValue2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey5", null);  // @Feature(params = {"testKey5:"})
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size()); // impl3 & duplicate impl3
    }

    // "params" of @Feature only has value
    // Has ":" assign
    @Test
    void testActiveWithoutKey() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put(null, "testValue6"); //  @Feature(params = {":testValue6"})
        List list = loader.getByFeature(null, map);
        assertEquals(0, list.size());
    }

    // Duplicate key or value match the same impl
    @Test
    void testMultiKeyValue1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey5", null); // impl3 & duplicate impl3
        map.put("testKey4", null); // impl3 & duplicate impl3
        map.put(null, "testValue6"); // impl3 & duplicate impl3
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size());
    }

    @Test
    void testMultiKeyValue2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null);  // impl1 & impl2
        map.put("testKey4", null);  // impl3 & duplicate impl3
        map.put(null, "testValue6"); // impl3 & duplicate impl3
        List list = loader.getByFeature(null, map);
        assertEquals(4, list.size());
    }

    @Test
    void testMultiKeyValue3() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null); // impl1 & impl2
        map.put("testKey7", null);  // not match
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size());
    }

    @Test
    void testMultiKeyValue4() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey7", null);  // not match
        map.put("testKey8", null);  // not match
        List list = loader.getByFeature(null, map);
        assertEquals(0, list.size());
    }

    @Test
    void testMultiKeyValue5() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey1", "testValue10");  // not match
        map.put("testKey3", "");  // impl3 & duplicate impl3
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size()); // impl3
    }

    @Test
    void testMultiKeyValue6() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("", "");  // not match
        map.put("testKey3", "");  // impl3 & duplicate impl3
        List list = loader.getByFeature(null, map);
        assertEquals(2, list.size());
    }

    @Test
    void testGroupAndMultiKeyValue1() {  // group match && key-value not match
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey7", null);  // not match
        map.put("testKey8", null);  // not match
        List list = loader.getByFeature("group2", map);
        assertEquals(0, list.size());
    }

    @Test
    void testGroupAndMultiKeyValue2() {  // group not match && key-value match
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null);  // impl1 & impl2
        map.put("testKey4", null);  // impl3
        List list = loader.getByFeature("group10", map);
        assertEquals(0, list.size());
    }

    @Test
    void testGroupAndMultiKeyValue3() {  // group match && key-value match
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null);  // impl1 & impl2
        map.put("testKey4", null);  // impl3 & duplicate impl3
        String group = "group2"; // impl2 & impl3 & duplicate impl3
        List list = loader.getByFeature(group, map);
        assertEquals(3, list.size()); // impl2 & impl3 & duplicate impl3
    }

    @Test
    void testGroupAndMultiKeyValue4() {  // group match && key-value match
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", null);  // impl1 & impl2
        map.put("testKey4", null);  // impl3 & duplicate impl3
        String group = "group1"; // impl1 & impl2
        List list = loader.getByFeature(group, map);
        assertEquals(2, list.size());  // impl1 & impl2
    }

    @Test
    void testGroupAndKeyValue() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2 ", "testValue2");  // impl1 & impl2
        String group = " group1 "; // impl1 & impl2
        List list = loader.getByFeature(group, map);
        assertEquals(2, list.size());  // impl1 & impl2
    }

    @Test
    void testTrim1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2 ", null);  // impl1 & impl2
        String group = " group1 "; // impl1 & impl2
        List list = loader.getByFeature(group, map);
        assertEquals(2, list.size());  // impl1 & impl2
    }

    @Test
    void testTrim2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", "   testValue2");  // impl1 & impl2
        String group = " group1 "; // impl1 & impl2
        List list = loader.getByFeature(group, map);
        assertEquals(2, list.size());  // impl1 & impl2
    }

    @Test
    void testTrim3() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", " testValue2");  // impl1 & impl2
        String group = "test group"; // impl2
        List list = loader.getByFeature(group, map);
        assertEquals(1, list.size());  // impl2
    }

    @Test
    void testTrim4() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", "test value2");  // not match
        String group = " group1 "; // impl1 & impl2
        List list = loader.getByFeature(group, map);
        assertEquals(0, list.size());  // impl1 & impl2
    }

    @Test
    void testGetAllExtensions() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        List list = loader.getAll();
        // impl1 & impl2 & impl3 & duplicate impl3
        assertEquals(5, list.size());
    }

    @Test
    void testExcludeMultipleParams() {
        // ParamImpl1 & ParamImpl2
        SpiLoader loader = SpiLoader.cached(TestExcludeParamSpi.class);
        // ExcludeParamImpl1 & ExcludeParamImpl2
        Map<String, String> map = new HashMap<>();
        map.put("testKey2", "testValue2");
        map.put("testKey3", "");
        List list = loader.getByTags(map);
        // ParamImpl1 & ParamImpl2
        assertEquals(2, list.size());
    }

    /**
     * 3) Test cases of "order" of @Feature
     * <p>
     * Test sorting of @Feature, need to REVISE file: "/META-INF/services/esa.commons.spi.sort.activate
     * .TestFeatureSortSpi"
     */

    // Need to REVISE the file to uncomment the lines belows "# test1"
    // Basic test sort of @Feature
    @Test
    void testFeatureSort1() throws Exception {
        SpiLoader loader = SpiLoader.cached(TestFeatureSortSpi.class);
        List list = loader.getAll();
        if (list != null && list.size() != 0) {
            assertTrue(list.get(0) instanceof FeatureSortSpiImpl3);
            assertTrue(list.get(1) instanceof FeatureSortSpiImpl2);
            assertTrue(list.get(2) instanceof FeatureSortSpiImpl1);
        } else {
            throw new Exception("META-INF file is not configured correctly");
        }
    }

}
