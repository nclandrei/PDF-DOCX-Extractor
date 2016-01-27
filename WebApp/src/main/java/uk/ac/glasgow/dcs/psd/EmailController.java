package uk.ac.glasgow.dcs.psd;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

	@Autowired
	private SmtpMailSender smtpMailSender;

	@RequestMapping(value="/send-mail", method= RequestMethod.POST)
    @ResponseBody
	public String sendMail(@RequestParam("bug") String bug,
                           @RequestParam("device") String device,
                           @RequestParam("name") String name) throws MessagingException {
		smtpMailSender.send("guteamx.contact@gmail.com", "Bug report on " + device + " by " + name,
                bug);

		return "Your message was sent successfully.";
//		return "Your message was sent successfully.<br> Do you want to return to "
//                + "<a href=\"/\">Main page?</a>";
	}


}
