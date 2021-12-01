package esa.commons;

import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.util.function.Function;

final class ConfigUtilsImpl implements ConfigUtils {
    static final Logger logger = LoggerFactory.getLogger(ConfigUtilsImpl.class);
    private final Function<String, String> getter;

    ConfigUtilsImpl(Function<String, String> getter) {
        Checks.checkNotNull(getter, "getter");
        this.getter = getter;
    }

    @Override
    public String getStr(String name) {
        Checks.checkNotEmptyArg(name, "name");
        return getter.apply(name);
    }
}
