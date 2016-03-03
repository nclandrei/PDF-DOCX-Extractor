package uk.ac.glasgow.dcs.psd;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = {
        "uk.ac.glasgow.dcs.psd.Components",
        "uk.ac.glasgow.dcs.psd.Controllers",
        "uk.ac.glasgow.dcs.psd.Models",})
@EnableAutoConfiguration
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

    private static final String[] RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        String userDir = System.getProperties().getProperty("user.dir");
        String prefix = "file:";
        registry.addResourceHandler("/**").addResourceLocations(RESOURCE_LOCATIONS);
        registry.addResourceHandler("/qunit/**")
                .addResourceLocations(prefix + userDir + "/src/test/qunit/");
    }
}
