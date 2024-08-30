package com.github.paopaoyue.wljmod;

import com.github.paopaoyue.wljmod.api.IWljCaller;
import io.github.paopaoyue.mesh.rpc.RpcAutoConfiguration;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(
        exclude = GsonAutoConfiguration.class
)
public class WljServiceApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(WljServiceApplication.class, args);
    }

    public static void initializeClient(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
        context = new SpringApplicationBuilder()
                .sources(WljServiceApplication.class)
                .resourceLoader(new DefaultResourceLoader(classLoader))
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .properties(loadProperties("application-client.properties"))
                .run();
    }

    public static IWljCaller getCaller() {
        assert RpcAutoConfiguration.getProp().isClientEnabled();
        return context.getBean(IWljCaller.class);
    }

    private static Properties loadProperties(String propertiesFile) {
        try (InputStream inputStream = WljServiceApplication.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found: " + propertiesFile);
            }
            Properties properties = new Properties();
            properties.load(inputStream);

            for (String key : properties.stringPropertyNames()) {
                System.setProperty(key, properties.getProperty(key));
            }

            return properties;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + propertiesFile, e);
        }
    }

}
