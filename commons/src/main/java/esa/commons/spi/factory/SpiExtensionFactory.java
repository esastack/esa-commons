package esa.commons.spi.factory;

import esa.commons.spi.SPI;
import esa.commons.spi.SpiLoader;

import java.util.Optional;

public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            Optional<T> byName = SpiLoader.getByName(type, name);
            if (byName.isPresent()) {
                return byName.get();
            }
        }
        return null;
    }
}
