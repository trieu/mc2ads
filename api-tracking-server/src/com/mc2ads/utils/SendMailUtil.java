package com.mc2ads.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailUtil {
	private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
	
	//FIXME
	private static final String SMTP_AUTH_USER = "";
	private static final String SMTP_AUTH_PWD = "";

	public static void main(String[] args) throws Exception {
		new SendMailUtil().test();
	}

	public void test() throws Exception {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(props, auth);
		// uncomment for debugging infos to stdout
		// mailSession.setDebug(true);
		Transport transport = mailSession.getTransport();

		MimeMessage message = new MimeMessage(mailSession);

		Multipart multipart = new MimeMultipart("alternative");

		BodyPart part1 = new MimeBodyPart();
		part1.setText("This is multipart mail and u read part1......");

		BodyPart part2 = new MimeBodyPart();
		part2.setContent(
				"<b>This is multipart mail and u read part2......</b>",
				"text/html");

		multipart.addBodyPart(part1);
		multipart.addBodyPart(part2);

		message.setContent(multipart);
		message.setFrom(new InternetAddress("trieu@mc2ads.com"));
		message.setSubject("test email");
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				"tantrieuf31@gmail.com"));

		transport.connect();
		transport.sendMessage(message,
				message.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}
