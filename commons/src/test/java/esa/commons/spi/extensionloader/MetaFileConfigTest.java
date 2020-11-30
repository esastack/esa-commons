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
import esa.commons.spi.extensionloader.duplicate.TestDuplicateSpi;
import esa.commons.spi.extensionloader.duplicate.TestDuplicateSpi2;
import esa.commons.spi.extensionloader.noparam.NoDefaultSpi;
import esa.commons.spi.extensionloader.noparam.WrongDefaultSpi;
import esa.commons.spi.extensionloader.wrapper.NormalInterface;
import esa.commons.spi.extensionloader.wrapper.SpiWrapperImpl1;
import esa.commons.spi.extensionloader.wrapper.SpiWrapperImpl2;
import esa.commons.spi.extensionloader.wrapper.TestWrapperSpi;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * This class contains two types of test: 1) class configuration of META-INF file 2) name configuration of META-INF
 * file
 */
class MetaFileConfigTest {
    /**
     * 1) Test class from META-INF file of getExtension()
     */

    // Class not exists
    @Test
    void testClassFromFileNotExists() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // Ignore the class which not exists in project
        // No extension match with name (myClass), return null

        Optional optional = loader.getByName("myClass");
        assertFalse(optional.isPresent());
    }

    // Class exists but not a impl of @SPI interface
    @Test
    void testClassFromFileNotImplOfInterface() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // Ignore the class which is not a implementation of @SPI
        // No extension match with name (normalClass), return null

        Optional optional = loader.getByName("normalClass");
        assertFalse(optional.isPresent());
    }

    // Duplicate class in file
    @Test
    void testClassFromFileDuplicate() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // Duplicate class is not recommended in config file
        // Extension should be unique with different name

        loader.getByName("innerImpl3").ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));
    }

    // Inner static class
    @Test
    void testStaticInnerClassFromFile() {
        SpiLoader loader = SpiLoader.cached(NormalInterface.class);

        Optional optional = loader.getByName("normal1");
        assertTrue(optional.isPresent());
    }

    // Inner nonstatic class
    @Test
    void testNonstaticInnerClassFromFile() {

        assertThrows(IllegalStateException.class, () -> {
            SpiLoader loader = SpiLoader.cached(NormalInterface.class);
            NormalInterface extension = (NormalInterface) loader.getByName("normal2").get();
        });
    }

    /**
     * 2) Test name from META-INF file of getExtension()
     */

    // Name from META-INF file is expected
    // file line: "name = xxx.xxx.xxx.ClassName"
    @Test
    void testNameFromCallerIsExpected() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);

        loader.getByName("innerImpl2").ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));
    }

    // (1) Name from META-INF file is NOT given
    // (2) Name from function is given and is not full class name
    // file line: "xxx.xxx.xxx.ClassName"
    @Test
    void testNameFromCallerNotGivenAndNameFromFuncGiven1() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // If name from file is not given, the extension name for the class is "class full name"
        // Throw a Exception with message: "No extension match with name (normalClass)" --> "class full name" is
        // expected

        Optional optional = loader.getByName("innerImpl5");
        assertFalse(optional.isPresent());
    }

    // (1) Name from META-INF file is NOT given
    // (2) Name from function is given and is full class name
    // file line: "xxx.xxx.xxx.ClassName"
    @Test
    void testNameFromCallerNotGivenAndNameFromFuncGiven2() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // If name from file is not given, the extension name for the class is "class full name"
        // Throw a Exception with message: "No extension match with name (normalClass)" --> "class full name" is
        // expected

        loader.getByName("esa.commons.spi.extensionloader.wrapper.SpiInnerImpl5").ifPresent(ext ->
                assertTrue(ext instanceof SpiWrapperImpl2 || ext instanceof SpiWrapperImpl1));
    }

    // (1) Name from META-INF file is NOT given
    // (2) Name from function is NOT given
    // (3) @SPI NO default impl
    // file line: "xxx.xxx.xxx.ClassName"
    @Test
    void testNameFromCallerNotGiven1() {
        SpiLoader loader = SpiLoader.cached(NoDefaultSpi.class);

        Optional optional = loader.getByName(null);
        assertFalse(optional.isPresent());
    }

    // (1) Name from META-INF file is NOT given
    // (2) Name from function is NOT given
    // (3) @SPI has a default impl but does not match with full class name
    // file line: "xxx.xxx.xxx.ClassName"
    @Test
    void testNameFromCallerNotGiven2() {
        SpiLoader loader = SpiLoader.cached(WrongDefaultSpi.class);
        // "wrongName" is @SPI default extension, but a full class name is expected

        Optional optional = loader.getByName(null);
        assertFalse(optional.isPresent());
    }

    // Name from META-INF file is NOT given but has "="
    // file line: "=xxx.xxx.xxx.ClassName"  --> as a wrong class name
    @Test
    void testNameFromFileEmptyAndFileLineHasEqualSign() {
        SpiLoader loader = SpiLoader.cached(TestWrapperSpi.class);
        // Throw a Exception with message: "No extension match with name (wrongName)"
        // "=esa.rpc.common.spi.SpiInnerImpl4" is expected

        Optional optional = loader.getByName("innerImpl4");
        assertFalse(optional.isPresent());
    }
}
