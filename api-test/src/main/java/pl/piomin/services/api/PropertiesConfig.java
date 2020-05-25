package pl.piomin.services.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created By Gorantla, Eresh on 17/Dec/2019
 **/
@Configuration
@ConfigurationProperties(prefix = "app.data")
public class PropertiesConfig {

    private String test;
    private String name;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}