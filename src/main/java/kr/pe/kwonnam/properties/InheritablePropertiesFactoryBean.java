package kr.pe.kwonnam.properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.util.Properties;

/**
 * Inheritable and ${} placeholder support Properties FactoryBean.
 * <p/>
 * ${key:defaultValue} is replaced by the parent or System properties' 'key' property value.
 * <p/>
 * <ol>
 * <li><code>extends=classpath:/configs/common.properties</code> : extends common.properties file.</li>
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
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_PREFIX;
    private String valueSeparator = DEFAULT_VALUE_SEPARATOR;

    /**
     * if ignoreUnresolvablePlaceholders is false, it will throw {@link java.lang.IllegalArgumentException}
     * when ${key} is missing in the parent properties.
     */
    private boolean ignoreUnresolvablePlaceholders = DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS;

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }

    @Override
    public Properties getObject() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Properties> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}