package uk.ac.glasgow.dcs.psd;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * This method starts application and
 * is used for configuration of component
 * mapping.
 *
 * There should not be any multi-line
 * mappings in this class.
 */

@Configuration
@ComponentScan(basePackages = {
        "uk.ac.glasgow.dcs.psd.Components",
        "uk.ac.glasgow.dcs.psd.Controllers",
        "uk.ac.glasgow.dcs.psd.Models",})
@EnableAutoConfiguration
@Controller
public class WebAppApplication {

    /**
     * <h1>Return Index page</h1>
     * Returns index.html page when when root of the domain is accessed.
     *
     * @return          resources/public/index.html
     */
    @RequestMapping("/")
    String home() { return "/index.html"; }

    /**
     * <h1>Starts Spring application</h1>
     * Main method to start web application
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebAppApplication.class, args);
    }

}