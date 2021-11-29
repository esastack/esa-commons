package esa.commons.spi.factory;

import esa.commons.spi.SPI;

@SPI
public interface ExtensionFactory {
    <T> T getExtension(Class<T> type, String name);
}
