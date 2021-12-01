package esa.commons;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Unity class for retrieving config properties from {@link System#getProperties()}, {@link System#getenv()} and
 * custom {@link Map}.
 */
public interface ConfigUtils {

    /**
     * Default instance of {@link ConfigUtils} which retrieves properties from {@link System#getProperties()} firstly,
     * and then retrieves from {@link System#getenv()} if missing. Especially, when we try to retrieve value from
     * {@link System#getenv()}, we will replace the '_' in the key with a '.' and try to retrieve value from
     * {@link System#getenv()}.
     */
    static ConfigUtils get() {
        return Builder.DEFAULT;
    }

    /**
     * Creates a new instance by {@link Builder}.
     */
    static Builder custom() {
        return new Builder();
    }

    /**
     * Gets a string property or {@code null} if a property with given {@code name} has not been configured.
     */
    String getStr(String name);

    /**
     * Gets a string property or {@code def} if a property with given {@code name} has not been configured.
     */
    default String getStr(String name, String def) {
        String v = getStr(name);
        return v == null ? def : v;
    }

    /**
     * Gets a boolean property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Boolean getBool(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        try {
            return Boolean.parseBoolean(v);
        } catch (RuntimeException e) {
            ConfigUtilsImpl.logger.debug("Unable to parse bool property '{}':{}", name, v, e);
            return null;
        }
    }

    /**
     * Gets a boolean property or {@code def} if a property with given {@code name} has not been configured.
     */
    default boolean getBool(String name, boolean def) {
        Boolean v = getBool(name);
        return v == null ? def : v;
    }

    /**
     * Gets a integer property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Integer getInt(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        try {
            return Integer.parseInt(v);
        } catch (RuntimeException e) {
            ConfigUtilsImpl.logger.debug("Unable to parse int property '{}':{}", name, v, e);
            return null;
        }
    }

    /**
     * Gets a integer property or {@code def} if a property with given {@code name} has not been configured.
     */
    default int getInt(String name, int def) {
        Integer v = getInt(name);
        return v == null ? def : v;
    }

    /**
     * Gets a long property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Long getLong(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        try {
            return Long.parseLong(v);
        } catch (RuntimeException e) {
            ConfigUtilsImpl.logger.debug("Unable to parse long property '{}':{}", name, v, e);
            return null;
        }
    }

    /**
     * Gets a long property or {@code def} if a property with given {@code name} has not been configured.
     */
    default long getLong(String name, long def) {
        Long v = getLong(name);
        return v == null ? def : v;
    }

    /**
     * Gets a double property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Double getDouble(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        try {
            return Double.parseDouble(v);
        } catch (RuntimeException e) {
            ConfigUtilsImpl.logger.debug("Unable to parse double property '{}':{}", name, v, e);
            return null;
        }
    }

    /**
     * Gets a double property or {@code def} if a property with given {@code name} has not been configured.
     */
    default double getDouble(String name, double def) {
        Double v = getDouble(name);
        return v == null ? def : v;
    }

    /**
     * Gets a duration property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Duration getDuration(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }

        int unitIdx = v.length() - 1;
        while (unitIdx > -1) {
            char c = v.charAt(unitIdx);
            if (Character.isDigit(c)) {
                break;
            }
            unitIdx -= 1;
        }

        String unit = v.substring(unitIdx + 1).trim();
        String value = v.substring(0, v.length() - unit.length()).trim();

        if (value.isEmpty()) {
            ConfigUtilsImpl.logger.debug("Unable to parse duration property '{}':{}", name, v);
            return null;
        }

        try {
            long number = Long.parseLong(value);
            switch (unit) {
                case "":
                case "ms":
                    return Duration.ofMillis(number);
                case "s":
                    return Duration.ofSeconds(number);
                case "m":
                    return Duration.ofMinutes(number);
                case "h":
                    return Duration.ofHours(number);
                case "d":
                    return Duration.ofDays(number);
                default:
                    throw new IllegalArgumentException("Invalid duration: " + v);
            }
        } catch (RuntimeException e) {
            ConfigUtilsImpl.logger.debug("Unable to parse duration property '{}':{}", name, v, e);
            return null;
        }
    }

    /**
     * Gets a duration property or {@code def} if a property with given {@code name} has not been configured.
     */
    default Duration getDuration(String name, Duration def) {
        Duration v = getDuration(name);
        return v == null ? def : v;
    }

    /**
     * Gets a list property or {@code null} if a property with given {@code name} has not been configured.
     */
    default List<String> getList(String name) {
        String v = getStr(name);
        if (StringUtils.isEmpty(v)) {
            return null;
        }

        String[] items = v.split(",");
        List<String> list = new ArrayList<>(items.length);
        for (String item : items) {
            item = item.trim();
            if (StringUtils.isNotEmpty(item)) {
                list.add(item);
            }
        }
        return list;
    }

    /**
     * Gets a list property or {@code def} if a property with given {@code name} has not been configured.
     */
    default List<String> getList(String name, List<String> def) {
        List<String> v = getList(name);
        return v == null ? def : v;
    }

    /**
     * Gets a map property or {@code null} if a property with given {@code name} has not been configured.
     */
    default Map<String, String> getMap(String name) {
        List<String> list = getList(name);
        if (list == null) {
            return null;
        }

        Map<String, String> map = new LinkedHashMap<>(list.size());
        for (String s : list) {
            String[] kv = s.split("=", 2);
            String k;
            if (kv.length != 2 || (k = kv[0].trim()).isEmpty()) {
                ConfigUtilsImpl.logger.debug("Unable to parse map property '{}':{}", name, getStr(name));
                return null;
            }
            map.put(k, StringUtils.trim(kv[1]));
        }
        return map;
    }

    /**
     * Gets a map property or {@code def} if a property with given {@code name} has not been configured.
     */
    default Map<String, String> getMap(String name, Map<String, String> def) {
        Map<String, String> v = getMap(name);
        return v == null ? def : v;
    }

    class Builder {

        private static final ConfigUtils DEFAULT =
                new Builder().readFromSystemProperty().readFromEnv().build();

        private final List<Function<String, String>> getters = new LinkedList<>();

        public Builder readFromEnv() {
            getters.add(System::getenv);
            // System.getenv() is case insensitive
            // io_esastack_url -->  io.esastak.url
            getters.add(k -> System.getenv(k.replace('.', '_')));
            return this;
        }

        public Builder readFromSystemProperty() {
            getters.add(System::getProperty);
            return this;
        }

        public Builder readFromMap(Map<String, String> map) {
            Checks.checkNotNull(map, "map");
            getters.add(map::get);
            return this;
        }

        public ConfigUtils build() {
            Function<String, String> getter;

            if (getters.isEmpty()) {
                // default to read system properties firstly and the read env.
                readFromSystemProperty().readFromEnv();
            }

            if (getters.size() == 1) {
                getter = getters.get(0);
            } else {
                final Function<String, String>[] theGetters = getters.toArray(new Function[0]);
                getter = s -> {
                    String v = null;
                    for (Function<String, String> g : theGetters) {
                        v = g.apply(s);
                        if (StringUtils.isNotEmpty(v)) {
                            break;
                        }
                    }
                    return v;
                };
            }
            return new ConfigUtilsImpl(getter);
        }
    }

}
