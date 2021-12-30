/*
 * Copyright 2021 OPPO ESA Stack Project
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
package esa.commons.spi;

import esa.commons.spi.feature.FeatureBean;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Feature(name = "bean1")
class SpiLoaderTest {
    @Test
    void testExtensionPair() {
        SpiLoader.ExtensionPair bean = new SpiLoader.ExtensionPair("bean", String.class);
        assertTrue(bean.equals(bean));
        assertFalse(bean.equals(null));
        assertFalse(bean.equals(1));
    }

    @Test
    void testWrapperClassInfo() {
        SpiLoader.WrapperClassInfo<String> stringWrapperClassInfo = new SpiLoader.WrapperClassInfo<>(String.class);
        SpiLoader.WrapperClassInfo<String> stringWrapperClassInfo1 = new SpiLoader.WrapperClassInfo<>(String.class);
        assertEquals(String.class, stringWrapperClassInfo.getClazz());
        assertEquals(0, stringWrapperClassInfo.getOrder());
        assertTrue(stringWrapperClassInfo.equals(stringWrapperClassInfo1));
        assertFalse(stringWrapperClassInfo.equals(Integer.class));
        assertEquals(0, stringWrapperClassInfo.compareTo(stringWrapperClassInfo1));
        assertEquals(stringWrapperClassInfo.hashCode(), stringWrapperClassInfo1.hashCode());
    }

    @Test
    void testFeatureInfo() {
        Feature annotation = SpiLoaderTest.class.getAnnotation(Feature.class);
        SpiLoader.FeatureInfo bean1 = new SpiLoader.FeatureInfo("bean1", annotation);
        assertTrue(bean1.equals(bean1));
        assertFalse(bean1.equals(1));
    }

    @Test
    void testGetByFeature() {
        SpiLoader<FeatureBean> cached = SpiLoader.cached(FeatureBean.class);
        assertEquals(Optional.empty(), cached.getByFeature("bean3", "consumer", false));
        Optional<FeatureBean> byFeature = cached.getByFeature("bean1", "consumer", false);
        Optional<FeatureBean> byFeature1 = cached.getByFeature("bean2", "provider", true);
        assertNotNull(byFeature.get());
        assertNotNull(byFeature1.get());

        List<FeatureBean> byFeature2 = cached.getByFeature(Arrays.asList("bean1"), "consumer", false, null, false);
        assertEquals(1, byFeature2.size());
        assertEquals(byFeature.get(), byFeature2.get(0));
    }
}
