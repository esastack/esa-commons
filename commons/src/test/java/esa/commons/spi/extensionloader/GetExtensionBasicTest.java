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
import esa.commons.spi.extensionloader.noparam.NoDefaultSpi;
import esa.commons.spi.extensionloader.noparam.RightDefaultSpi;
import esa.commons.spi.extensionloader.noparam.RightDefaultSpiImpl;
import esa.commons.spi.extensionloader.noparam.WrongDefaultSpi;
import esa.commons.spi.extensionloader.sort.wrapper.TestWrapperSortSpi;
import esa.commons.spi.extensionloader.sort.wrapper.WrapperSortSpiImpl1;
import esa.commons.spi.extensionloader.sort.wrapper.WrapperSortSpiImpl2;
import esa.commons.spi.extensionloader.sort.wrapper.WrapperSortSpiImpl3;
import esa.commons.spi.extensionloader.sort.wrapper.WrapperSortSpiImpl4;
import esa.commons.spi.extensionloader.sort.wrapper.WrapperSortSpiImpl5;
import esa.commons.spi.extensionloader.wrapper.NormalClass;
import esa.commons.spi.extensionloader.wrapper.NormalInterface;
import esa.commons.spi.extensionloader.wrapper.SpiWrapperImpl1;
import esa.commons.spi.extensionloader.wrapper.SpiWrapperImpl2;
import esa.commons.spi.extensionloader.wrapper.TestWrapperSpi;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains three types of test:
 * 1) get extension loader
 * 2) get extension by name
 * 3) get extension with no param
 * 4) test sorting of wrapper classes
 */
class GetExtensionBasicTest {
    /**
     * 1) Test class type input of getting extension loader function
     */

    @Test
    void testWhenClassTypeNull() {
        assertThrows(NullPointerException.class, () -> SpiLoader.cached(null));
    }

    @Test
    void testWhenClassTypeNotInterface() {
        assertThrows(IllegalArgumentException.class, () -> SpiLoader.cached(NormalClass.class));
    }

    @Test
    void testWhenClassTypeNotSPI() {
        SpiLoader loader = SpiLoader.cached(NormalInterface.class);
        assertNotNull(loader);
    }

    @Test
    void testWhenClassTypeExpected() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        assertNotNull(loader);
    }

    @Test
    void testWhenClassTypeCached() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        SpiLoader loader2 = SpiLoader.cached(TestWrapperSpi.class);
        assertSame(loader, loader2);
    }

    /**
     * 2) Test all cases input of getExtension(String name)
     */

    // Input name is NOT empty and is expected
    // Default name of @SPI is expected
    @Test
    void testNameFromCallerNotEmptyAndExpected() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);

        loader.getByName("innerImpl1").ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));
    }

    // Input name NOT exists in META-INF file
    @Test
    void testNameFromCallerNotExists() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        Optional optional = loader.getByName("myExtension");
        assertFalse(optional.isPresent());
    }

    // Input name is wrapper class name and exists in META-INF file
    @Test
    void testNameFromCallerIsWrapperClassName() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);

        Optional optional = loader.getByName("wrapper1");
        assertFalse(optional.isPresent());
    }

    // Input name is NULL or empty string
    // @SPI Default name is expected
    @Test
    void testNameFromCallerEmpty1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // A default extension could be found

        loader.getByName("").ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));

        loader.getByName(null).ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));
    }

    // Input name is NULL
    // @SPI has NO default implementation
    @Test
    void testNameFromCallerEmptyAndNoDefaultName() {
        SpiLoader loader = SpiLoader.cached(NoDefaultSpi.class);

        Optional optional = loader.getByName(null);
        assertFalse(optional.isPresent());
    }

    // Input name is NULL
    // @SPI Default name can NOT match the implementation config in META-INF file
    @Test
    void testNameFromCallerEmptyAndDefaultNameNotMatch() {
        SpiLoader loader = SpiLoader.cached(WrongDefaultSpi.class);

        Optional optional = loader.getByName(null);
        assertFalse(optional.isPresent());
    }

    /**
     * 3) Test NO parameter getExtension()
     */




    // Default name of @SPI could match the implementation config in META-INF file
    @Test
    void testNoNameParameterAndDefaultNameRight() {
        SpiLoader loader = SpiLoader.cached(RightDefaultSpi.class);

        loader.getDefault().ifPresent(ext -> assertTrue(ext instanceof RightDefaultSpiImpl));
    }

    // Default name of @SPI can NOT match the impl config in META-INF file
    @Test
    void testNoNameParameterAndDefaultNameWrong() {
        SpiLoader loader = SpiLoader.cached(WrongDefaultSpi.class);

        Optional optional = loader.getDefault();
        assertFalse(optional.isPresent());
    }

    // No default impl of @SPI
    @Test
    void testNoNameParameterAndNoDefaultName() {
        SpiLoader loader = SpiLoader.cached(NoDefaultSpi.class);

        Optional optional = loader.getDefault();
        assertFalse(optional.isPresent());
    }

    /**
     * 4) test sorting of wrapper classes
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSort() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSortSpi.class);
        try {
            Field wrapperClassesField = loader.getClass().getDeclaredField("wrapperClasses");
            wrapperClassesField.setAccessible(true);
            TreeSet set = (TreeSet) wrapperClassesField.get(loader);
            List list = new ArrayList(set);

            Class<?>[]  innerClasses = loader.getClass().getDeclaredClasses();
            Field clazzField = innerClasses[0].getDeclaredField("clazz");
            clazzField.setAccessible(true);
            assertEquals(clazzField.get(list.get(0)), WrapperSortSpiImpl5.class);
            assertEquals(clazzField.get(list.get(1)), WrapperSortSpiImpl4.class);
            assertEquals(clazzField.get(list.get(2)), WrapperSortSpiImpl3.class);
            assertEquals(clazzField.get(list.get(3)), WrapperSortSpiImpl2.class);
            assertEquals(clazzField.get(list.get(4)), WrapperSortSpiImpl1.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDuplicate() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSortSpi.class);
        try {
            Field wrapperClassesField = loader.getClass().getDeclaredField("wrapperClasses");
            wrapperClassesField.setAccessible(true);
            TreeSet set = (TreeSet) wrapperClassesField.get(loader);
            Iterator it = set.iterator();

            Class<?>[]  innerClasses = loader.getClass().getDeclaredClasses();
            Field clazzField = innerClasses[0].getDeclaredField("clazz");
            clazzField.setAccessible(true);

            // "WrapperSortSpiImpl5" is duplicate in file
            int impl5Count = 0;
            while (it.hasNext()) {
                Object obj = it.next();
                if (clazzField.get(obj).equals(WrapperSortSpiImpl5.class)) {
                    impl5Count++;
                }
            }

            // Duplicate classes will be change to one(wrapper class should be unique)
            assertEquals(impl5Count, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
