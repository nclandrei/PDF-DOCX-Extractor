package uk.ac.glasgow.dcs.psd.ControllerTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.glasgow.dcs.psd.Controllers.EmailController;
import uk.ac.glasgow.dcs.psd.WebAppApplication;

import javax.mail.MessagingException;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Test class for the Email Controller that sends email
 * with specified parameters
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebAppApplication.class)
@PropertySource("classpath:application.properties ")
public class EmailControllerTest {

    /**
     * Get an instance of EmailController
     */
    @Autowired
    EmailController emailController = new EmailController();

    /**
     * Try to message using SendMail method
     * @throws MessagingException if message could not be send
     */
    @Test
    public void testSendMail() throws MessagingException {

        String re = emailController.sendMail("bug", "device", "name");

        assertEquals("message sent", re, "Your message was sent successfully.");

    }
}