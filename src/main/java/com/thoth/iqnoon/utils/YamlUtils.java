package com.thoth.iqnoon.utils;

import java.util.Map;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class YamlUtils {

    public Map<String, Object> getMaps(String fileName) {
        try {
            YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
            yamlMapFactoryBean.setResources(new ClassPathResource(fileName));
            return yamlMapFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Cannot read yaml" + fileName, e);
        }
    }

    public Properties getProperties(String fileName) {
        try {
            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(new ClassPathResource(fileName));
            return yamlPropertiesFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Cannot read yaml" + fileName, e);
        }
    }
}



