package uk.ac.glasgow.dcs.psd.ControllerTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Constants;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uk.ac.glasgow.dcs.psd.ApplicationConfiguration;
import uk.ac.glasgow.dcs.psd.WebAppApplication;

/**
 * Test case for HomeController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@PropertySource("classpath:application.properties")
@WebAppConfiguration
public class HomeControllerTest {
    @Autowired WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * Try to open base url and expect HTML page
     * with HTML code 200.
     * @throws Exception if an error occurs
     */
    @Test
    public void getHome() throws Exception {
        this.mockMvc.perform(get("/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
