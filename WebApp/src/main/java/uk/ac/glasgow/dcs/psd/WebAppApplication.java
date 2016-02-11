package uk.ac.glasgow.dcs.psd;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Configuration
@ComponentScan(basePackages = {"uk.ac.glasgow.dcs.psd.Components","uk.ac.glasgow.dcs.psd.Controllers"})
@EnableAutoConfiguration
@Controller
public class WebAppApplication {

    @RequestMapping("/")
    String home() { return "/index.html"; }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebAppApplication.class, args);
    }

}