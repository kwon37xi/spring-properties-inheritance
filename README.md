spring-properties-inheritance
=============================

Spring properties inheritance is for Java Spring applications which use properties file(*.properties/*.xml)
for their configurations.

When we have multiple module project, there will be a common configuration and
other configurations which are specific for each module.

I thought specific configuration properties file can extend the common configuration properties file.
So even though I read only the specific properties file, the common configuration is included
automatically.

## Features

1. A Properties file can extend parent properties file, then child properties object includes parent properties.
1. The parent can have it's parent again.
1. Child property can override parent's property value.
1. ${placeholder} will be replaced by the parent property value or System property value.
1. File location is defined by Spring resource location style like 'claspath:/path/some.properties'.
1. Support normal properties file(*.properties) and xml properties file(*.xml)

## How to

1. Copy [InheritablePropertiesFactoryBean.java](https://github.com/kwon37xi/spring-properties-inheritance/blob/master/src/main/java/kr/pe/kwonnam/properties/InheritablePropertiesFactoryBean.java)
into your project source directory.
1. Create Spring bean.
1. I prefer using properties with SpEL like #{configurationProperties['some.key']}.
1. You can change extendKey ,placeholder prefix/suffix, etc. refer to the source.

## Example

### bean configuration
```xml
<bean id="configurationProperties" class="kr.pe.kwonnam.properties.InheritablePropertiesFactoryBean">
    <property name="location" value="classpath:/child-properties.xml" />
    <property name="extendKey" value="__extends__" /> <!-- actually you don't need this line. It's just an example' -->
</bean>
```

### parent-properties.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <entry key="project.name">Spring Properties Inheritance</entry>
    <entry key="project.home">${java.io.tmpdir}/project</entry>
    <entry key="key.dir">${java.io.tmpdir}/keys</entry>
</properties>
```
### child-properties.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <entry key="__extends__">classpath:/parent-properties.xml</entry>
    <entry key="project.name">Sub project</entry> <!-- override parent's project.name -->
    <entry key="subproject.home">${project.home}/subproject</entry>
    <entry key="userproject.home">${project.home}/users/${user.name}</entry> <!-- user.name from System Properties -->
</properties>
```

### configurationProperties bean will have the following properties
1. project.name
1. project.home
1. key.dir
1. subproject.home
1. userproject.home