package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.ac.glasgow.dcs.psd.Components.SmtpMailSender;
import uk.ac.glasgow.dcs.psd.WebAppApplication;

import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing if SmtpMailSender actually sends a message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class SmtpMailSenderTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            Properties props = new Properties();
            javaMailSender.setUsername("guteamx.contact@gmail.com");
            javaMailSender.setPassword("thisisasuperstrongpassword");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.EnableSSL.enable","true");
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.socketFactory.port", "587");
            javaMailSender.setJavaMailProperties(props);
            return javaMailSender;
        }
    }

    private SmtpMailSender smtpSender;
    @Autowired
    private JavaMailSender javaSender;
    private Exception ex;


    @Before
    public void setUp () throws IOException {
        smtpSender = new SmtpMailSender();
        ex = null;
    }

    @Test
    public void testEmailSender () throws NoSuchFieldException, IllegalAccessException {
        try {
            Field field = smtpSender.getClass().getDeclaredField("javaMailSender");
            field.setAccessible(true);
            field.set(smtpSender, javaSender);
            smtpSender.send("guteamx.contact@gmail.com", "this is a test subject", "this is a test body");
        }
        catch (MessagingException e) {
            ex = e;
        }
        assertEquals(null, ex);
    }
}

