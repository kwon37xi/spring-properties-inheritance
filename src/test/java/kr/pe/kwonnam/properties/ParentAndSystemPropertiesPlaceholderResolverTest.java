package kr.pe.kwonnam.properties;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Author: KwonNam Son(kwon37xi@gmail.com)
 */
public class ParentAndSystemPropertiesPlaceholderResolverTest {

    public static final String PROPS_HELLO_WORLD = "안녕! 세상아!";
    public static final String PROPS_MEMBER_NAME = "KwonNam Son";
    public static final String PROPS_MEMBER_EMAIL = "kwon37xi@gmail.com";
    public static final String PROPS_USER_HOME = "/home/myhome";
    private InheritablePropertiesFactoryBean.ParentAndSystemPropertiesPlaceholderResolver resolver;
    private Properties parentProperties;

    @Before
    public void setup() {
        parentProperties = new Properties();
        parentProperties.put("hello.world", PROPS_HELLO_WORLD);
        parentProperties.put("member.name", PROPS_MEMBER_NAME);
        parentProperties.put("member.email", PROPS_MEMBER_EMAIL);

        resolver = new InheritablePropertiesFactoryBean.ParentAndSystemPropertiesPlaceholderResolver(parentProperties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setParentProperties_must_not_null() {
        new InheritablePropertiesFactoryBean.ParentAndSystemPropertiesPlaceholderResolver(null);
    }

    @Test
    public void resolvePlaceholder_from_parent() {
        assertThat(resolver.resolvePlaceholder("hello.world"), is(PROPS_HELLO_WORLD));
        assertThat(resolver.resolvePlaceholder("member.email"), is(PROPS_MEMBER_EMAIL));
    }

    @Test
    public void resolvePlaceholder_from_system() {
        assertThat(resolver.resolvePlaceholder("user.home"), is(System.getProperty("user.home")));
        assertThat(resolver.resolvePlaceholder("java.io.tmpdir"), is(System.getProperty("java.io.tmpdir")));
    }

    @Test
    public void resolvePlaceholder_parent_first() {
        parentProperties.setProperty("user.home", PROPS_USER_HOME);

        assertThat(resolver.resolvePlaceholder("user.home"), is(PROPS_USER_HOME));
    }

    @Test
    public void resolvePlaceholder_no_value() {
        assertThat(resolver.resolvePlaceholder("some.value.which.does.not.exist"), nullValue());
    }
}
