package uk.ac.glasgow.dcs.psd.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for links
 * inside home page.
 */
@Controller
public class HomeController {

    /**
     * <h1>Return Index page</h1>
     * Returns index.html page when when root of the domain is accessed.
     *
     * @return          resources/public/index.html
     */
    @RequestMapping("/")
    String home() { return "/index.html"; }

}
