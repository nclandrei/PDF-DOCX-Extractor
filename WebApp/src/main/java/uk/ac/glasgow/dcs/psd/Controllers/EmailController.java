package uk.ac.glasgow.dcs.psd.Controllers;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.glasgow.dcs.psd.Components.SmtpMailSender;

/**
 * Controller responsible for sending
 * email messages
 */
@RestController
public class EmailController {

	@Autowired
	private SmtpMailSender smtpMailSender;

    /**
     * <h1>Send bug report</h1>
     * Allows to send bug reports
     *
     * @param bug          description of a bug
     * @param device       device used when bug occurred
     * @param name         name of the reporter
     * @return             success/error message
     */
	@RequestMapping(value="/send-mail", method= RequestMethod.POST)
    @ResponseBody
	public String sendMail(@RequestParam("bug") String bug,
								  @RequestParam("device") String device,
								  @RequestParam("name") String name) throws MessagingException {
		smtpMailSender.send("guteamx.contact@gmail.com", "Bug report on " + device + " by " + name,
                bug);

		return "Your message was sent successfully.";
	}
}
