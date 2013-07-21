package kr.pe.kwonnam.properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.util.Properties;

/**
 * Inheritable and ${} placeholder support Properties FactoryBean.
 * <p/>
 * Author: KwonNam Son(kwon37xi@gmail.com}
 */
public class InheritablePropertiesFactoryBean implements FactoryBean<Properties>, ResourceLoaderAware {
    /**
     * *.properties or *.xml file location.
     */
    private String location;
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public Properties getObject() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Properties> getObjectType() {
        return Properties.class;
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