package kr.pe.kwonnam.properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.IOException;
import java.util.Properties;

/**
 * Inheritable and ${} placeholder support Properties FactoryBean.
 * <p/>
 * ${key:defaultValue} is replaced by the parent or System properties' 'key' property value.
 * <p/>
 * <ol>
 * <li><code>__extends__=classpath:/configs/common.properties</code> : extends common.properties file.</li>
 * <li><code>some.key=${parent.key:defaultValue}</code> use parent or System properties' 'parent.key' value.
 * If parent/System properties does not have 'parent.key' property, defaultValue will be used.</li>
 * </ol>
 * <p/>
 * Author: KwonNam Son(kwon37xi@gmail.com)
 */
public class InheritablePropertiesFactoryBean implements FactoryBean<Properties>, ResourceLoaderAware {
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PACEHOLDER_SUFFIX = "}";
    public static final String DEFAULT_VALUE_SEPARATOR = ":";
    public static final String DEFAULT_EXTEND_KEY = "__extends__";
    public static final boolean DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;

    /**
     * .properties or *.xml file location.
     */
    private String location;

    /**
     * Spring ResourceLoader
     */
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = DEFAULT_PACEHOLDER_SUFFIX;
    private String valueSeparator = DEFAULT_VALUE_SEPARATOR;
    private String extendKey = DEFAULT_EXTEND_KEY;

    /**
     * if ignoreUnresolvablePlaceholders is false, it will throw {@link java.lang.IllegalArgumentException}
     * when ${key} is missing in the parent properties.
     */
    private boolean ignoreUnresolvablePlaceholders = DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS;

    private PropertyPlaceholderHelper propertyPlaceholderHelper;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
    }

    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public String getExtendKey() {
        return extendKey;
    }

    public void setExtendKey(String extendKey) {
        this.extendKey = extendKey;
    }

    public boolean isIgnoreUnresolvablePlaceholders() {
        return ignoreUnresolvablePlaceholders;
    }

    public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Properties getObject() throws Exception {
        propertyPlaceholderHelper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator,
                ignoreUnresolvablePlaceholders);

        Properties properties = loadProperties(location);

        return properties;
    }

    private Properties loadProperties(String location) throws IOException {
        Assert.notNull(location, "location cannot be null");

        Properties properties = new Properties();

        if (location.toLowerCase().endsWith(".xml")) {
            properties.loadFromXML(resourceLoader.getResource(location).getInputStream());
        } else {
            properties.load(resourceLoader.getResource(location).getInputStream());
        }

        Properties parentProperties = loadParentProperties(properties);

        resolvePlaceholders(properties, parentProperties);

        parentProperties.putAll(properties);
        properties = parentProperties;
        return properties;
    }

    private Properties loadParentProperties(Properties childProperties) throws IOException {
        Properties parentProperties = null;
        String parentLocation = childProperties.getProperty(extendKey);

        if (parentLocation != null) {
            childProperties.remove(extendKey);
            parentProperties = loadProperties(parentLocation);
        } else {
            parentProperties = new Properties();
        }

        return parentProperties;
    }

    private void resolvePlaceholders(Properties properties, Properties parentProperties) {
        PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver = new ParentAndSystemPropertiesPlaceholderResolver(parentProperties);

        for (Object key : properties.keySet()) {
            String value = properties.getProperty((String) key);
            String resolvedValue = propertyPlaceholderHelper.replacePlaceholders(value, placeholderResolver);
            properties.setProperty((String) key, resolvedValue);
        }
    }

    @Override
    public Class<? extends Properties> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * Author: KwonNam Son(kwon37xi@gmail.com)
     */
    public static class ParentAndSystemPropertiesPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private Properties parentProperties;

        public ParentAndSystemPropertiesPlaceholderResolver(Properties parentProperties) {
            Assert.notNull(parentProperties, "parentProperties must not be null.");
            this.parentProperties = parentProperties;
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            String value = parentProperties.getProperty(placeholderName);
            if (value == null) {
                value = System.getProperty(placeholderName);
            }
            return value;
        }
    }
}