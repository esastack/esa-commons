
package esa.commons.spi;

import esa.commons.Checks;
import esa.commons.ClassUtils;
import esa.commons.StringUtils;
import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI unity class, allows to load SPI instances by given predicates lazily. The SPI specification files should be
 * maintained in {@link #ESA_INTERNAL_DIRECTORY}, {@link #ESA_DIRECTORY} or {@link #SERVICE_DIRECTORY}, and there's no
 * difference between these folders except the name.
 *
 * @param <T> type of target instance.
 * @see SPI
 * @see Feature
 */
public class SpiLoader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiLoader.class);

    private static final String ESA_INTERNAL_DIRECTORY = "META-INF/esa/internal/";
    private static final String ESA_DIRECTORY = "META-INF/esa/";
    private static final String SERVICE_DIRECTORY = "META-INF/services/";

    /**
     * Spi loader cached by SPI type
     */
    private static final ConcurrentHashMap<Class<?>, SpiLoader<?>> LOADER_CACHE = new ConcurrentHashMap<>();

    /**
     * Spi extension objects cached by name (All extensions has already wrapped by all the wrappers)
     */
    private final ConcurrentHashMap<String, T> extensionCache = new ConcurrentHashMap<>();

    /**
     * Inner spi extension classes
     */
    private final Map<String, Class<? extends T>> extensionClasses = new HashMap<>();

    /**
     * Sorted wrapper class Info
     */
    private final Set<WrapperClassInfo<?>> wrapperClasses = new TreeSet<>();

    /**
     * Sorted feature info
     */
    private final Set<FeatureInfo> featuresCache = new TreeSet<>();
    private final Class<T> type;
    private final String defaultExtension;

    private SpiLoader(Class<T> type) {
        this.type = type;

        if (type.isAnnotationPresent(SPI.class)) {
            this.defaultExtension = type.getAnnotation(SPI.class).value();
        } else {
            this.defaultExtension = null;
        }

        // Load classes from META-INF directory
        loadFromDir(ESA_INTERNAL_DIRECTORY);
        loadFromDir(ESA_DIRECTORY);
        loadFromDir(SERVICE_DIRECTORY);
    }

    /**
     * Get extension loader with class type.
     */
    @SuppressWarnings("unchecked")
    public static <T> SpiLoader<T> cached(Class<T> type) {
        Checks.checkNotNull(type, "type");
        Checks.checkArg(type.isInterface(), "Type (" + type + ") is NOT an interface!");
        if (!LOADER_CACHE.containsKey(type)) {
            SpiLoader<T> loader = new SpiLoader<>(type);
            LOADER_CACHE.putIfAbsent(type, loader);
        }
        return (SpiLoader<T>) LOADER_CACHE.get(type);
    }

    /**
     * Static method to get default extension of class type.
     *
     * @see #getDefault()
     */
    public static <T> Optional<T> getDefault(Class<T> type) {
        return cached(type).getDefault();
    }

    /**
     * Static method to get extension by class type and name.
     *
     * @see #getByName(String)
     */
    public static <T> Optional<T> getByName(Class<T> type, String name) {
        return cached(type).getByName(name);
    }

    /**
     * Static method to get all the extensions by class type.
     *
     * @see #getAll()
     */
    public static <T> List<T> getAll(Class<T> type) {
        return getAll(type, false);
    }

    /**
     * Static method to get all the extensions by class type.
     *
     * @see #getAll()
     */
    public static <T> List<T> getAll(Class<T> type, boolean continueIfErr) {
        return cached(type).getAll(continueIfErr);
    }

    /**
     * Get all extensions of SPI, if fail to load an specific extension, then abort and throw the exception.
     */
    public List<T> getAll() {
        return getAll(false);
    }

    /**
     * @param continueIfErr whether continue to load other extensions if fail to load an specific extension.
     *                      Get all extensions of SPI.
     */
    public List<T> getAll(boolean continueIfErr) {
        return getByFeature(null, true, null, true, continueIfErr);
    }

    /**
     * Get extension without parameter.
     */
    public Optional<T> getDefault() {
        if (StringUtils.isBlank(defaultExtension)) {
            return Optional.empty();
        }
        return getByName(defaultExtension);
    }

    /**
     * Get extension by name. 'synchronized' is re-entrant, make sure a spi implementation is create and init only once
     * Map::computeIfAbsent should not be used here because User may call SpiLoader::getByName in the construct method
     * of an Extension.
     */
    public Optional<T> getByName(String name) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(name = defaultExtension)) {
            return Optional.empty();
        }
        if (!extensionCache.containsKey(name)) {
            synchronized (this) {
                if (!extensionCache.containsKey(name)) {
                    T ext = newExtension(name);
                    if (ext != null) {
                        extensionCache.put(name, ext);
                    }
                }
            }
        }
        return Optional.ofNullable(extensionCache.get(name));
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>Only match with the "groups" of @{@link Feature}.</p>
     *
     * @param group expected group name of extension, match with the "groups" of @{@link Feature}
     * @see #getByGroup(String, boolean, boolean)
     */
    public List<T> getByGroup(String group) {
        return getByGroup(group, false, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>Only match with the "groups" of @{@link Feature}.</p>
     *
     * @param group          expected group name of extension, match with the "groups" of @{@link Feature}
     * @param matchIfMissing whether return all the extensions if group mismatch.
     * @see #getByGroup(String, boolean, boolean)
     */
    public List<T> getByGroup(String group, boolean matchIfMissing) {
        return getByGroup(group, matchIfMissing, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>- Only match with the "groups" of @{@link Feature}.</p>
     * <p>- Selectively get the extensions whose "groups" of @{@link Feature} is not configured.</p>
     *
     * @param group          expected group name of extension, match with the "groups" of @{@link Feature}
     * @param matchIfMissing whether return the extensions whose @{@link Feature} "groups" is not configured. If
     *                       "matchIfMissing" is TRUE, the result extension list will contains:
     *                       <ul>
     *                       <li>the extensions match with "group" and "tags"</li>
     *                       <li>the extensions whose @{@link Feature} "groups" is not configured</li>
     *                       </ul>
     * @param continueIfErr  whether continue if fail to load one of the extensions.
     */
    public List<T> getByGroup(String group, boolean matchIfMissing, boolean continueIfErr) {
        return getByFeature(group, matchIfMissing, null, false, continueIfErr);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>Exactly match the parameters with @{@link Feature} annotation's fields.</p>
     *
     * @param tags expected key-values of extension, match with the "tags" of @{@link Feature}
     * @see #getByTags(Map, boolean, boolean)
     */
    public List<T> getByTags(Map<String, String> tags) {
        return getByTags(tags, false, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>Exactly match the parameters with @{@link Feature} annotation's fields.</p>
     *
     * @param tags           expected key-values of extension, match with the "tags" of @{@link Feature}
     * @param matchIfMissing whether return all the extensions if tags mismatch.
     * @see #getByTags(Map, boolean, boolean)
     */
    public List<T> getByTags(Map<String, String> tags, boolean matchIfMissing) {
        return getByTags(tags, matchIfMissing, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>- Only match with the "tags" of @{@link Feature}.</p>
     * <p>- Selectively get the extensions whose "tags" of is not configured.</p>
     *
     * @param tags           expected key-values of extension, match with the "tags" of @{@link Feature}
     * @param matchIfMissing whether return the extensions whose "tags" of @{@link Feature} is not configured. If
     *                       "matchIfMissing" is TRUE, the result extension list will contains:
     *                       <ul>
     *                       <li>the extensions match with "group" and "tags"</li>
     *                       <li>the extensions whose @{@link Feature} "tags" is not configured</li>
     *                       </ul>
     * @param continueIfErr  whether continue if fail to load one of the extensions.
     */
    public List<T> getByTags(Map<String, String> tags, boolean matchIfMissing, boolean continueIfErr) {
        return getByFeature(null, false, tags, matchIfMissing, continueIfErr);
    }

    /**
     * <p>Get expected featured extensions, if fail to load an specific extension,
     * then abort and throw the exception. </p>
     *
     * @see #getByFeature(String, Map, boolean)
     */
    public List<T> getByFeature(String group, Map<String, String> tags) {
        return getByFeature(group, tags, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>Exactly match the parameters with @{@link Feature} annotation's fields.</p>
     *
     * @param group         expected group name of extension, match with the "groups" of @{@link Feature}
     * @param tags          expected key-values of extension, match with the "tags" of @{@link Feature}
     * @param continueIfErr whether continue if fail to load one of the extensions.
     */
    public List<T> getByFeature(String group, Map<String, String> tags, boolean continueIfErr) {
        return getByFeature(group, false, tags, false, continueIfErr);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>whether return the extensions whose group of tags mismatch.</p>
     *
     * @see #getByFeature(String, boolean, Map, boolean, boolean)
     */
    public List<T> getByFeature(String group,
                                boolean matchGroupIfMissing,
                                Map<String, String> tags,
                                boolean matchTagIfMissing) {
        return getByFeature(group, matchGroupIfMissing, tags, matchTagIfMissing, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>- Match the input parameters with @{@link Feature} annotation's fields.</p>
     * <p>- Selectively get the extensions whose "groups" or "tags" of @{@link Feature} is not configured.</p>
     *
     * @param group               expected group name of extension, match with the "groups" of @{@link Feature}
     * @param tags                expected key-values of extension, match with the "tags" of @{@link Feature}
     * @param matchGroupIfMissing whether return the extensions whose @{@link Feature} "groups" is not configured
     * @param matchTagIfMissing   whether return the extensions whose @{@link Feature} "tags" is not configured
     *                            <p>It will contains these three parts:</p>
     *                            <ul>
     *                            <li>the extensions match with "group" and "tags"</li>
     *                            <li>the extensions whose "groups" of @{@link Feature} is not configured, if
     *                            "matchTagIfMissing" is TRUE</li>
     *                            <li>the extensions whose "tags" of @{@link Feature} is not configured, if
     *                            "matchTagIfMissing" is TRUE</li>
     *                            </ul>
     * @param continueIfErr       whether continue if fail to load one of the extensions.
     */
    public List<T> getByFeature(String group,
                                boolean matchGroupIfMissing,
                                Map<String, String> tags,
                                boolean matchTagIfMissing,
                                boolean continueIfErr) {
        return getByFeature(null, group, matchGroupIfMissing, tags, matchTagIfMissing, continueIfErr);
    }

    /**
     * <p>Get expected featured extensions.</p>
     *
     * @see #getByFeature(Collection, String, boolean, Map, boolean, boolean)
     */
    public List<T> getByFeature(Collection<String> namesFilter,
                                String group,
                                boolean matchGroupIfMissing,
                                Map<String, String> tags,
                                boolean matchTagIfMissing) {
        return getByFeature(namesFilter, group, matchGroupIfMissing, tags, matchTagIfMissing, false);
    }

    /**
     * <p>Get expected featured extensions.</p>
     * <p>- Match the input parameters with @{@link Feature} annotation's fields.</p>
     * <p>- Selectively get the extensions whose "groups" or "tags" of @{@link Feature} is not configured.</p>
     *
     * @param namesFilter         when group is match, names started with prefix "-" in namesFilter will be force
     *                            removed from while others will be force added into the result list.
     * @param group               expected group name of extension, match with the "groups" of @{@link Feature}
     * @param tags                expected key-values of extension, match with the "tags" of @{@link Feature}
     * @param matchGroupIfMissing whether return the extensions whose @{@link Feature} "groups" is not configured
     * @param matchTagIfMissing   whether return the extensions whose @{@link Feature} "tags" is not configured
     *                            <p>It will contains these three parts:</p>
     *                            <ul>
     *                            <li>the extensions match with "group" and "tags"</li>
     *                            <li>the extensions whose "groups" of @{@link Feature} is not configured, if
     *                            "matchTagIfMissing" is TRUE</li>
     *                            <li>the extensions whose "tags" of @{@link Feature} is not configured, if
     *                            "matchTagIfMissing" is TRUE</li>
     *                            </ul>
     * @param continueIfErr       whether continue if fail to load one of the extensions.
     */
    public List<T> getByFeature(Collection<String> namesFilter,
                                String group,
                                boolean matchGroupIfMissing,
                                Map<String, String> tags,
                                boolean matchTagIfMissing,
                                boolean continueIfErr) {
        List<T> featuredExtensions = new LinkedList<>();
        for (FeatureInfo featureInfo : featuresCache) {
            if (!isMatchGroup(featureInfo.groups, group, matchGroupIfMissing) ||
                    isForceExclude(namesFilter, featureInfo.name)) {
                continue;
            }

            if (isForceInclude(namesFilter, featureInfo.name) ||
                    isMatchTags(featureInfo.tagsMap, featureInfo.excludeTagsMap, tags, matchTagIfMissing)) {
                try {
                    getByName(featureInfo.name).ifPresent(featuredExtensions::add);
                } catch (Throwable e) {
                    if (continueIfErr) {
                        LOGGER.error("Failed to get instance of {}, named {}", type.getTypeName(), featureInfo.name, e);
                    } else {
                        throw e;
                    }
                }
            }
        }
        return featuredExtensions;
    }

    private boolean isForceInclude(Collection<String> namesFilter, String name) {
        if (namesFilter == null || namesFilter.isEmpty()) {
            return false;
        }

        return namesFilter.contains(name);
    }

    private boolean isForceExclude(Collection<String> namesFilter, String name) {
        if (namesFilter == null || namesFilter.isEmpty()) {
            return false;
        }

        return namesFilter.contains("-" + name);
    }


    /**
     * <p>Get expected featured extensions.</p>
     *
     * @param name                name of extension in spi file
     * @param group               expected group name of extension, match with the "groups" of @{@link Feature}
     * @param matchGroupIfMissing whether return the extensions whose @{@link Feature} "groups" is not configured
     * @return match extension with Optional
     */
    public Optional<T> getByFeature(String name, String group, boolean matchGroupIfMissing) {
        for (FeatureInfo featureInfo : featuresCache) {
            if (!isMatchGroup(featureInfo.groups, group, matchGroupIfMissing)) {
                continue;
            }
            if (featureInfo.name.equals(name)) {
                return getByName(name);
            }

        }
        return Optional.empty();
    }

    /**
     * Create a new extension instance.
     */
    private T newExtension(String name) {
        Class<? extends T> extensionClass = extensionClasses.get(name);
        if (extensionClass == null) {
            return null;
        }
        try {
            T instance = extensionClass.newInstance();
            for (WrapperClassInfo<?> wrapperClassInfo : wrapperClasses) {
                instance = (T) wrapperClassInfo.getClazz().getConstructor(type).newInstance(instance);
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance of class (" + type + ") couldn't be instantiated", t);
        }
    }

    /**
     * Load meta info files from directory.
     */
    private void loadFromDir(String dir) {
        String fileName = dir + type.getName();
        try {
            Enumeration<URL> urls;
            final ClassLoader classLoader = ClassUtils.getClassLoader();

            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loading SPI resource: " + resourceUrl.getPath());
                    }
                    parseResource(classLoader, resourceUrl);
                }
            }
        } catch (Throwable t) {
            throw new IllegalStateException("An exception occurs when loading directory: " + fileName, t);
        }
    }

    /**
     * Parse resources configuration in file.
     */
    @SuppressWarnings("unchecked")
    private void parseResource(ClassLoader classLoader, URL resource) throws Exception {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {

                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    // Get string before '#'
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    String name = null;
                    int i = line.indexOf('=');
                    if (i > 0) {
                        name = line.substring(0, i).trim();
                        line = line.substring(i + 1).trim();
                    }
                    if (StringUtils.isBlank(name)) {
                        // Use full name of class as name
                        name = line;
                    }
                    if (line.length() > 0) {
                        Class<? extends T> oldClass = extensionClasses.get(name);
                        if (oldClass != null) {
                            if (!line.equals(oldClass.getName())) {
                                String errMsg = String.format("Different SPI extensions(%s and %s) of %s " +
                                        "has same name:%s", oldClass.getName(), line, type.getName(), name);
                                LOGGER.error(errMsg);
                                throw new IllegalStateException(errMsg);
                            }
                            LOGGER.warn("Different SPI extensions with same name({}) and class({}) loaded, " +
                                    "the one loaded from ({}) is ignored!", name, line, resource.toString());
                            continue;
                        }
                        final Class<?> clazz;
                        try {
                            clazz = Class.forName(line, false, classLoader);
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("Could not load extension for SPI " + type.getName(), e);
                            continue;
                        }
                        if (!type.isAssignableFrom(clazz)) {
                            LOGGER.error("{}(loaded by {}) could not be assigned to {}(loaded by {})",
                                    line, clazz.getClassLoader(), type.getName(), type.getClassLoader());
                            continue;
                        }
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Class " + clazz.getName() + " is loaded from " + resource.getPath());
                        }
                        putInCache(name, (Class<? extends T>) clazz);
                    }
                }
            }
        }
    }

    /**
     * Put parsed class info into cache.
     */
    private void putInCache(String name, Class<? extends T> clazz) {
        if (isWrapperClass(clazz, type)) {
            Feature wrapperFeature = clazz.getAnnotation(Feature.class);
            wrapperClasses.add(wrapperFeature == null ? new WrapperClassInfo<>(clazz) : new WrapperClassInfo<>(clazz,
                    wrapperFeature.order()));
        } else {
            // Duplicated spi definition, the next one will overwrite the previous one
            extensionClasses.put(name, clazz);
            Feature feature = clazz.getAnnotation(Feature.class);
            featuresCache.add(new FeatureInfo(name, feature));
        }
    }

    /**
     * Test if clazz is a wrapper class which has constructor with given class type as its only argument.
     */
    private boolean isWrapperClass(Class<?> clazz, Class<?> type) {
        try {
            clazz.getConstructor(type);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Check if group in @{@link Feature} matches with input group or not.
     */
    private boolean isMatchGroup(String[] featureGroups, String group, boolean matchIfMissing) {
        if (StringUtils.isBlank(group)) {
            return true;
        }

        if (matchIfMissing && (featureGroups == null || featureGroups.length == 0)) {
            return true;
        }

        if (featureGroups != null && featureGroups.length > 0) {
            for (String featureGroup : featureGroups) {
                if (featureGroup.trim().equals(group.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if tags in @{@link Feature} match with input tags or not.
     */
    private boolean isMatchTags(Map<String, String> featureTagsArr, Map<String, String> featureExcludeTagsArr,
                                Map<String, String> tags, boolean matchIfMissing) {
        if (tags == null || tags.isEmpty()) {
            return true;
        }

        // Match exclude tags first
        if (isMatchAnnotationTags(featureExcludeTagsArr, tags)) {
            return false;
        }

        if (matchIfMissing && (featureTagsArr == null || featureTagsArr.size() == 0)) {
            return true;
        }

        // If exclude tags not match, continue to match the feature tags
        return isMatchAnnotationTags(featureTagsArr, tags);
    }

    /**
     * Check if the 1) excludeTags or 2) tags in annotation @{@link Feature} match with the input tags or not.
     */
    private boolean isMatchAnnotationTags(Map<String, String> annotationTagsArr, Map<String, String> tags) {
        for (Map.Entry<String, String> annotationEntry : annotationTagsArr.entrySet()) {
            String annotationKey = annotationEntry.getKey();
            String annotationValue = annotationEntry.getValue();

            if (StringUtils.isBlank(annotationKey)) {
                continue;
            }

            for (Map.Entry<String, String> entry : tags.entrySet()) {
                if (StringUtils.isBlank(entry.getKey())) {
                    continue;
                }
                String key = entry.getKey().trim();
                String value = entry.getValue() == null ? "" : entry.getValue().trim();
                if (!annotationKey.equals(key)) {
                    continue;
                }
                if (StringUtils.isBlank(value) || annotationValue.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Feature info class
     */
    private static class FeatureInfo implements Comparable<FeatureInfo> {

        final String name;
        final String[] groups;
        final Map<String, String> tagsMap;
        final Map<String, String> excludeTagsMap;
        final int order;

        FeatureInfo(String name, Feature feature) {
            this.name = name;
            if (feature == null) {
                this.groups = new String[0];
                this.tagsMap = Collections.emptyMap();
                this.excludeTagsMap = Collections.emptyMap();
                this.order = 0;
            } else {
                this.groups = feature.groups();
                this.tagsMap = initTags(feature.tags());
                this.excludeTagsMap = initTags(feature.excludeTags());
                this.order = feature.order();
            }
        }

        @Override
        public int compareTo(FeatureInfo info) {
            return this.order <= info.order ? -1 : 1;
        }

        /**
         * Initialize tags.
         */
        private Map<String, String> initTags(String[] tags) {
            if (tags == null || tags.length == 0) {
                return Collections.emptyMap();
            }
            Map<String, String> parsed = new HashMap<>(tags.length);
            for (String tag : tags) {
                if (StringUtils.isBlank(tag)) {
                    continue;
                }
                String[] tagKeyValueArr = parseSingleTag(tag);
                parsed.put(tagKeyValueArr[0], tagKeyValueArr[1]);
            }
            return parsed;
        }

        /**
         * Parse the string of key-value pairs in tags or excludeTags of @{@link Feature}. This function will return a
         * parsed key-value array, eg: {key, value}
         */
        private String[] parseSingleTag(String featureTag) {
            String key = featureTag.trim();
            String value = "";
            if (featureTag.contains(":")) {
                String[] keyValueArr = featureTag.split(":");
                key = keyValueArr[0].trim();
                // if featureTag = "key:"
                if (keyValueArr.length == 1) {
                    value = "";
                } else {
                    value = keyValueArr[1].trim();
                }
            }
            return new String[]{key, value};
        }

        /**
         * It is invalid when this class object add in a TreeSet.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FeatureInfo)) {
                return false;
            }
            return this.name.equals(((FeatureInfo) obj).name);
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }

    /**
     * Wrapper info class
     */
    @SuppressWarnings("unchecked")
    private static class WrapperClassInfo<T> implements Comparable<WrapperClassInfo<T>> {

        final Class<? extends T> clazz;
        final int order;

        WrapperClassInfo(Class<? extends T> clazz) {
            this(clazz, 0);
        }

        WrapperClassInfo(Class<? extends T> clazz, int order) {
            this.clazz = clazz;
            this.order = order;
        }

        Class<? extends T> getClazz() {
            return clazz;
        }

        public int getOrder() {
            return order;
        }

        @Override
        public int compareTo(WrapperClassInfo<T> info) {
            // not allow to add same wrapper class
            if (this.clazz.equals(info.getClazz())) {
                return 0;
            }
            return this.order <= info.getOrder() ? -1 : 1;
        }

        /**
         * It is invalid when this class object add in a TreeSet.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WrapperClassInfo)) {
                return false;
            }
            return this.clazz.equals(((WrapperClassInfo<T>) obj).clazz);
        }

        @Override
        public int hashCode() {
            return this.clazz.hashCode();
        }
    }
}
