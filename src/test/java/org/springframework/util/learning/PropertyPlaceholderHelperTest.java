package org.springframework.util.learning;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

/**
 * Author: KwonNam Son(kwon37xi@gmail.com)
 */
public class PropertyPlaceholderHelperTest {
    private PropertyPlaceholderHelper helper;

    private Properties baseProperties;

    @Before
    public void setup() {
        baseProperties = new Properties();
        baseProperties.setProperty("user.name", "kwon37xi");
        baseProperties.setProperty("user.email", "kwon37xi@gmail.com");
        baseProperties.setProperty("user.home", "/home/kwon37xi");
        baseProperties.setProperty("log.basedir", "/home/kwon37xi/logs");

    }

    private void givenHelper(boolean ignoreUnresolvablePlaceholders) {
        helper = new PropertyPlaceholderHelper("${", "}", ":", ignoreUnresolvablePlaceholders);
    }

    private String resolve(String value) {
        return helper.replacePlaceholders(value, baseProperties);
    }

    @Test
    public void placeholderValues() {
        givenHelper(false);
        assertThat(resolve("${user.name}"), is("kwon37xi"));
        assertThat(resolve("${user.email}"), is("kwon37xi@gmail.com"));
        assertThat(resolve("${user.home}/tmp"), is("/home/kwon37xi/tmp"));
    }

    @Test
    public void placeholderValues_default() {
        givenHelper(false);
        assertThat(resolve("${user.my.home:/home/user}/test"), is("/home/user/test"));
        assertThat(resolve("${user.home:/home/user}/test"), is("/home/kwon37xi/test"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void placeholderValues_not_ignore_unresolved() {
        givenHelper(false);
        resolve("${user.not.exists}");
    }

    @Test
    public void placeholderValues_ignore_unresolved() {
        givenHelper(true);
        assertThat(resolve("${user.not.exists}/hello"), is("${user.not.exists}/hello"));
    }
}