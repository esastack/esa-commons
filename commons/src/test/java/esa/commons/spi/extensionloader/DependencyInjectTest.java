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
import esa.commons.spi.extensionloader.inject.ConstructorInjectBean;
import esa.commons.spi.extensionloader.inject.ConstructorInjectCycleBean;
import esa.commons.spi.extensionloader.inject.FiledInjectBean;
import esa.commons.spi.extensionloader.inject.SetMethodInjectBean;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyInjectTest {

    @Test
    void testInjectFiled() {
        SpiLoader<FiledInjectBean> cached = SpiLoader.cached(FiledInjectBean.class);
        assertNotNull(cached);
        Optional<FiledInjectBean> bean1Optional = cached.getByName("bean1");
        Optional<FiledInjectBean> bean2Optional = cached.getByName("bean2");
        assertTrue(bean1Optional.isPresent());
        assertTrue(bean2Optional.isPresent());
        FiledInjectBean bean1 = bean1Optional.get();
        FiledInjectBean bean2 = bean2Optional.get();
        assertEquals(bean1.getInject(), bean2);
        assertEquals(bean2.getInject(), bean1);
    }

    @Test
    void testInjectMethod() {
        SpiLoader<SetMethodInjectBean> cached = SpiLoader.cached(SetMethodInjectBean.class);
        assertNotNull(cached);
        Optional<SetMethodInjectBean> bean1Optional = cached.getByName("bean1");
        Optional<SetMethodInjectBean> bean2Optional = cached.getByName("bean2");
        assertTrue(bean1Optional.isPresent());
        assertTrue(bean2Optional.isPresent());
        SetMethodInjectBean bean1 = bean1Optional.get();
        SetMethodInjectBean bean2 = bean2Optional.get();
        assertEquals(bean1.getInject(), bean2);
        assertEquals(bean2.getInject(), bean1);
    }

    @Test
    void testConstructorInject() {
        SpiLoader<ConstructorInjectBean> cached = SpiLoader.cached(ConstructorInjectBean.class);
        assertNotNull(cached);
        Optional<ConstructorInjectBean> bean1Optional = cached.getByName("bean1");
        Optional<ConstructorInjectBean> bean2Optional = cached.getByName("bean2");
        assertTrue(bean1Optional.isPresent());
        assertTrue(bean2Optional.isPresent());
        ConstructorInjectBean constructorInjectBean1 = bean1Optional.get();
        ConstructorInjectBean constructorInjectBean2 = bean2Optional.get();
        assertEquals(constructorInjectBean1.getInject(), constructorInjectBean2);
    }

    @Test
    void testConstructorCycleInject() {
        SpiLoader<ConstructorInjectCycleBean> cached = SpiLoader.cached(ConstructorInjectCycleBean.class);
        assertNotNull(cached);
        assertThrows(IllegalStateException.class, () -> cached.getByName("bean1"));
    }
}
