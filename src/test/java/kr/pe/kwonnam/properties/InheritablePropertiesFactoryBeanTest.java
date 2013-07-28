package kr.pe.kwonnam.properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Author: KwonNam Son(kwon37xi@gmail.com)
 */
@RunWith(Parameterized.class)
public class InheritablePropertiesFactoryBeanTest {
    private InheritablePropertiesFactoryBean factoryBean;
    private String justPropertiesLocatoin;
    private String grandChildPropertiesLocation;
    private Properties props;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] params = new Object[][] {
                {
                        "classpath:/kr/pe/kwonnam/properties/just-properties.xml",
                        "classpath:/kr/pe/kwonnam/properties/grand-child-properties.xml"
                },
                {
                        "classpath:/kr/pe/kwonnam/properties/just.properties",
                        "classpath:/kr/pe/kwonnam/properties/grand-child.properties"
                },
                {
                        "file:src/test/resources/kr/pe/kwonnam/properties/just.properties",
                        "file:src/test/resources/kr/pe/kwonnam/properties/grand-child.properties"
                }
        };
        return Arrays.asList(params);
    }

    public InheritablePropertiesFactoryBeanTest(String justProperties, String grandChildProperties) {
        this.justPropertiesLocatoin = justProperties;
        this.grandChildPropertiesLocation = grandChildProperties;
    }

    @Before
    public void setup() {
        factoryBean = new InheritablePropertiesFactoryBean();
        factoryBean.setLocation(justPropertiesLocatoin);
    }

    private void givenLocation(String location) throws Exception {
        factoryBean.setLocation(location);
        props = factoryBean.getObject();
    }

    @Test
    public void isSingleton() {
        assertThat(factoryBean.isSingleton(), is(false));
    }

    @Test
    public void getObjectType() throws Exception {
        assertThat((Class) factoryBean.getObjectType(), sameInstance(Properties.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getProperty_no_location() throws Exception {
        factoryBean.setLocation(null);
        factoryBean.getObject();
    }

    @Test
    public void getProperty_no_parent() throws Exception {
        props = factoryBean.getObject();

        assertProp("member.name", "KwonNam Son");
        assertProp("member.email", "kwon37xi@gmail.com");
    }

    private void assertProp(String key, String expected) {
        assertThat(props.getProperty(key), is(expected));
    }

    @Test
    public void getProperty_with_parent() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp("member.id", "kwon37xi");
        assertProp("project.name", "Spring Properties Inheritance");
    }

    @Test
    public void getProperty_with_parent_override() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp("test.override", "overridden");
        assertProp("test.override.triple", "overridden by grand child");
    }

    @Test
    public void getProperty_with_parent_remove_extend_key() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp(factoryBean.getExtendKey(), null);
    }

    @Test
    public void getProperty_placeholder_from_parent() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp("test.placeholder.parent", "parent value/child value/grand child value");
        assertProp("test.placeholder.grand_parent", "grandparent/grandchild");
        assertProp("test.placeholder.member.info","kwon37xi-KwonNam Son-kwon37xi@gmail.com-Spring Properties Inheritance");
    }

    @Test
    public void getProperty_placeholder_from_systemProperties() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp("project.home", System.getProperty("java.io.tmpdir") + "/project");
        assertProp("test.placeholder.parent.system", System.getProperty("java.io.tmpdir") + "/kwon37xi");
    }

    @Test
    public void getProperty_placeholder_from_parent_override_systemProperties() throws Exception {
        givenLocation(grandChildPropertiesLocation);

        assertProp("test.placeholder.parent.override.system", "KOREAN!!");
    }
}