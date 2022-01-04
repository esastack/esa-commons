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

import esa.commons.spi.factory.Inject;
import esa.commons.spi.inject.ConstructorInjectBean;
import esa.commons.spi.inject.ConstructorInjectCycleBean;
import esa.commons.spi.inject.FiledInjectBean;
import esa.commons.spi.inject.FiledInjectBean1;
import esa.commons.spi.inject.MixedInjectBean;
import esa.commons.spi.inject.NonInjectBean;
import esa.commons.spi.inject.OneInjectBean;
import esa.commons.spi.inject.SetMethodInjectBean;
import esa.commons.spi.inject.ZeroInjectBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testNonException() {
        SpiLoader<NonInjectBean> cached = SpiLoader.cached(NonInjectBean.class);
        assertNotNull(cached);
        assertThrows(IllegalStateException.class, () -> cached.getByName("bean1"));
        assertThrows(IllegalStateException.class, () -> cached.getByName("bean2"));
    }

    @Test
    void testName() {
        SpiLoader<MixedInjectBean> cached = SpiLoader.cached(MixedInjectBean.class);
        assertThrows(IllegalStateException.class, () -> cached.getByName("bean1"));
        assertThrows(IllegalStateException.class, () -> cached.getByName("bean3"));

        Optional<MixedInjectBean> bean2 = cached.getByName("bean2");
        assertTrue(bean2.isPresent());
        assertNull(((MixedInjectBean2) bean2.get()).bean);
        assertNull(((MixedInjectBean2) bean2.get()).zeroInjectBean);

        List<OneInjectBean> all = SpiLoader.getAll(OneInjectBean.class);
        assertEquals(1, all.size());
        Optional<MixedInjectBean> bean4 = cached.getByName("bean4");
        assertTrue(bean4.isPresent());
        assertEquals(all.get(0), ((MixedInjectBean4) bean4.get()).bean);

        Optional<MixedInjectBean> bean5 = cached.getByName("bean5");
        assertTrue(bean5.isPresent());
        assertNull(((MixedInjectBean5) bean5.get()).bean);

        Optional<MixedInjectBean> bean6 = cached.getByName("bean6");
        assertTrue(bean6.isPresent());
        Optional<FiledInjectBean> filedInjectBean = SpiLoader.getByName(FiledInjectBean.class, "bean1");
        assertTrue(filedInjectBean.isPresent());
        assertEquals(((MixedInjectBean6) bean6.get()).bean, filedInjectBean.get());

        Optional<MixedInjectBean> bean7 = cached.getByName("bean7");
        assertTrue(bean7.isPresent());
        assertEquals(((MixedInjectBean7) bean7.get()).bean, filedInjectBean.get());

        assertThrows(IllegalStateException.class, () -> cached.getByName("bean8"));

        Optional<MixedInjectBean> bean9 = cached.getByName("bean9");
        assertTrue(bean9.isPresent());
        assertNull(((MixedInjectBean9) bean9.get()).bean);

        Optional<MixedInjectBean> bean10 = cached.getByName("bean10");
        assertTrue(bean10.isPresent());
        assertNull(((MixedInjectBean10) bean10.get()).list);
    }

    static class OneInjectBean1 implements OneInjectBean {
    }

    @Feature(name = "bean1")
    static class MixedInjectBean1 implements MixedInjectBean {
        @Inject
        private FiledInjectBean bean;
    }

    @Feature(name = "bean2")
    static class MixedInjectBean2 implements MixedInjectBean {
        @Inject(require = false)
        FiledInjectBean bean;

        @Inject(require = false)
        ZeroInjectBean zeroInjectBean;
    }

    @Feature(name = "bean3")
    static class MixedInjectBean3 implements MixedInjectBean {
        @Inject
        private ZeroInjectBean bean;
    }

    @Feature(name = "bean4")
    static class MixedInjectBean4 implements MixedInjectBean {
        @Inject
        OneInjectBean bean;
    }

    @Feature(name = "bean5")
    static class MixedInjectBean5 implements MixedInjectBean {
        @Inject(name = "bean", require = false)
        OneInjectBean bean;
    }

    @Feature(name = "bean6")
    static class MixedInjectBean6 implements MixedInjectBean {
        @Inject(name = "bean1")
        FiledInjectBean bean;
    }

    @Feature(name = "bean7")
    static class MixedInjectBean7 implements MixedInjectBean {
        @Inject
        FiledInjectBean1 bean;
    }

    @Feature(name = "bean8")
    static class MixedInjectBean8 implements MixedInjectBean {
        @Inject
        FieldInjectBean3 bean;
    }

    @Feature(name = "bean9")
    static class MixedInjectBean9 implements MixedInjectBean {
        @Inject(require = false)
        FieldInjectBean3 bean;
    }

    @Feature(name = "bean10")
    static class MixedInjectBean10 implements MixedInjectBean {
        List<String> list;

        @Inject
        public MixedInjectBean10(List<String> list) {
            this.list = list;
        }
    }

    static class FieldInjectBean3 implements FiledInjectBean {
        @Override
        public Object getInject() {
            return null;
        }
    }
}
