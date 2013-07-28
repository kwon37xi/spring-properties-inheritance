package kr.pe.kwonnam.properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Author: 손권남(kwon37xi@coupang.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/kr/pe/kwonnam/properties/test-applicationContext.xml")
public class InheritablePropertiesFactoryBeanSpringTest {

    @Resource(name = "inheritanceProperties")
    private Properties inheritanceProperties;

    @Value("#{inheritanceProperties['project.name']}")
    private String projectName;

    @Value("#{inheritanceProperties['test.placeholder.parent.override.system']}")
    private String overriddenValue;

    @Test
    public void checkSpelValue() {
        assertThat(projectName, is("Spring Properties Inheritance"));
        assertThat(overriddenValue, is("KOREAN!!"));
    }

    @Test
    public void checkSomeValues() {
        assertPropertyValue("test.placeholder.member.info", "kwon37xi-KwonNam Son-kwon37xi@gmail.com-Spring Properties Inheritance");
        assertPropertyValue("test.override.triple", "overridden by grand child");
        assertPropertyValue("member.id", "kwon37xi");
    }

    private void assertPropertyValue(String key, String expected) {
        assertThat(inheritanceProperties.getProperty(key),
                is(expected));

    }
}