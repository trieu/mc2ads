package com.mc2ads.utils;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

/**
 * Send email java util using Sendgrid Service
 * (http://sendgrid.com/docs/index.html)
 * 
 * @author trieu
 *
 */
public class MailUtil {
	
	private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
	
	//FIXME
	private static final String SMTP_AUTH_USER = "";
	private static final String SMTP_AUTH_PWD = "";
	private static final int SMTP_PORT = 587;
	private static final Properties PROPERTIES = new Properties();
	private static String X_SMTPAPI_JSON = ""; 							

	
	static {
		PROPERTIES.put("mail.transport.protocol", "smtp");
		PROPERTIES.put("mail.smtp.host", SMTP_HOST_NAME);
		PROPERTIES.put("mail.smtp.port", SMTP_PORT);
		PROPERTIES.put("mail.smtp.auth", "true");
		SmtpApiHeader smtpApiHeader = new SmtpApiHeader();
		smtpApiHeader.addFilterSetting("subscriptiontrack", "enable", "0");
		smtpApiHeader.addFilterSetting("clicktrack", "enable", "0");
		X_SMTPAPI_JSON = smtpApiHeader.asJSON();
	}
	
	/**
	 * send the email to the list of emailReceivers
	 * 
	 * @param from
	 * @param emailReceivers
	 * @param subject
	 * @param content
	 * @return
	 */
	public static boolean send(final String from, final Set<String> emailReceivers, final String subject,final String content)  {

		try {
			Authenticator auth = new SMTPAuthenticator();
			final Session mailSession = Session.getDefaultInstance(PROPERTIES, auth);
			final Transport transport = mailSession.getTransport();
			transport.connect();
			
			// uncomment for debugging infos to stdout
			// mailSession.setDebug(true);

			final ExecutorService pool = Executors.newFixedThreadPool(5);
			
			for (String to : emailReceivers) {
				final String toAddress = to; 
				
				System.out.println("send to: " + to);
				
				pool.execute(new Runnable() {
					
					@Override
					public void run() {						
						try {
							
							Multipart multipart = new MimeMultipart("alternative");

							BodyPart part2 = new MimeBodyPart();
							part2.setContent(content,"text/html");
							multipart.addBodyPart(part2);

							MimeMessage message = new MimeMessage(mailSession);
							message.addHeader("X-SMTPAPI", X_SMTPAPI_JSON);
							
							message.setContent(multipart);
							message.setFrom(new InternetAddress(from));
							message.setSubject(subject);
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
																			
							transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			}		
			pool.shutdown();
			while( ! pool.isTerminated() ){ }

			transport.close();
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean send(String from, String to, String subject, String content)  {
		Set<String> email = new HashSet<String>(1);
		email.add(to);
		return send(from, email, subject, content);
	}

	static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {

		Set<String> emails = new HashSet<String>();
		emails.add("trieu@mc2ads.com");

		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("first_name", "trieu");
		dataModel.put("verify_url", "http://mc2ads.com");
		dataModel.put("unsubscribe_url", "http://mc2ads.com");
		String mailContent = TemplateUtils.processModel(dataModel , "email/", "user-confirm.ftl");

		MailUtil.send("trieu@mc2ads.com", emails , "[Test] User Confirmation", mailContent);				
		Thread.sleep(6000);
	}
	public static void main1(String[] args) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");		
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imap.port", "993");
		props.setProperty("mail.imap.connectiontimeout", "5000");
		props.setProperty("mail.imap.timeout", "5000");
		try {
		  Session session = Session.getDefaultInstance(props, null);
		  Store store = session.getStore("imaps");
		  store.connect("imap.gmail.com", "trieu@mc2ads.com", "");//FIXME
		  System.out.println(store);

			Folder inbox = store.getFolder("Inbox");
			SearchTerm term = new SearchTerm() {
				@Override
				public boolean match(Message msg) {
					try {
						if(msg.getSubject().startsWith("[Standup] 3/08/2012")){
							return true;
						}						
					} catch (MessagingException e) {
						e.printStackTrace();
					}
					return false;
				}
			};;;

			inbox.open(Folder.READ_ONLY);
			int total = inbox.getMessageCount();
			int numberResult = 20;
			int start = total - numberResult;
			System.out.println("Total:" + total);
			Message messages[] = inbox.getMessages(start, total);
			//Message messages[] = inbox.search(term );
			System.out.println("messages.length: "+messages.length);
			for (Message msg : messages) {
				System.out.println("--------------------------FROM " + InternetAddress.toString(msg.getFrom()));
				Address[] addresses = msg.getReplyTo();
				System.out.println(msg.getSubject());
				for (Address address : addresses) {
					System.out.println(address.toString());
				}
			}
		  
		} catch (NoSuchProviderException e) {
		  e.printStackTrace();
		  System.exit(1);
		} catch (MessagingException e) {
		  e.printStackTrace();
		  System.exit(2);
		}
	}
}
