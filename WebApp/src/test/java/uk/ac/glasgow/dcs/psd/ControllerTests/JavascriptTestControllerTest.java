package uk.ac.glasgow.dcs.psd.ControllerTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.glasgow.dcs.psd.ApplicationConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test case for JavascriptTestController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@PropertySource("classpath:application.properties")
@WebAppConfiguration
public class JavascriptTestControllerTest {
    @Autowired WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * Try to open base url/qunit/qunit_setup.html
     * and expect HTML page with HTML code 200.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIndex() throws Exception {
        this.mockMvc.perform(get("/qUnit")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    /**
     * Try to open base url/qunit/tests.html
     * and expect text page with HTML code 200.
     * @throws Exception if an error occurs
     */
    @Test
    public void testJS() throws Exception {
        this.mockMvc.perform(get("/tests.js")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }
}
