package uk.ac.glasgow.dcs.psd.Components;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Component to send emails using Gmail API and java MailSender
 */

@Component
public class SmtpMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * <h1>Send email</h1> Allows to send email using Gmail
     *
     * @param to      address to send email to
     * @param subject subject of the email message
     * @param body    body of the email message
     */
    public void send(String to, String subject, String body)
            throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, true); // true indicates
        // multipart message
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(body, true); // true indicates html
        // continue using helper object for more functionalities like adding
        // attachments, etc.

        javaMailSender.send(message);
    }
}
