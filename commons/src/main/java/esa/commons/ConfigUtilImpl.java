package esa.commons;

import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.util.function.Function;

final class ConfigUtilImpl implements ConfigUtil {
    static final Logger logger = LoggerFactory.getLogger(ConfigUtilImpl.class);
    private final Function<String, String> getter;

    ConfigUtilImpl(Function<String, String> getter) {
        Checks.checkNotNull(getter, "getter");
        this.getter = getter;
    }

    @Override
    public String getStr(String name) {
        Checks.checkNotEmptyArg(name, "name");
        return getter.apply(name);
    }
}
