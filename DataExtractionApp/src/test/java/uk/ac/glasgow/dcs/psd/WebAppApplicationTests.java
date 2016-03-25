package uk.ac.glasgow.dcs.psd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Asserts that there is nothing obstructing the Spring
 * application from launching in the first place
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebAppApplication.class)
@WebAppConfiguration
public class WebAppApplicationTests {

    @Test
    public void contextLoads() {
        assert true;
    }

}
