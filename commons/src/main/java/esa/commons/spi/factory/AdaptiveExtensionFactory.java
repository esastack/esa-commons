package esa.commons.spi.factory;

import esa.commons.spi.SpiLoader;

import java.util.List;

public class AdaptiveExtensionFactory implements ExtensionFactory {

    private final List<ExtensionFactory> factories;

    public AdaptiveExtensionFactory() {
        SpiLoader<ExtensionFactory> loader = SpiLoader.cached(ExtensionFactory.class);
        factories = loader.getAll();
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        for (ExtensionFactory factory : factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }
}
