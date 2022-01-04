package esa.commons.spi.continueiferr;

import esa.commons.spi.Feature;

@Feature(groups = "TEST", tags = {"k1:v1", "k2:v2"})
public class TestContinueIfErrSpiImpl3 implements TestContinueIfErrSpi {

    public TestContinueIfErrSpiImpl3() {
        throw new RuntimeException();
    }
}
