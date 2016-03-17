package uk.ac.glasgow.dcs.psd.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible with qunit testing.
 */
@Controller
public class JavascriptTestController {

    /**
     * Routes /test to actual html page
     * @return          qunit_setup.html page
     */
    @RequestMapping("/qUnit")
    String testIndex() { return "/qunit/index.html"; }

    @RequestMapping("/tests.js")
    String testJS() { return "/qunit/tests.js"; }

}
