package com.seminaro.utility;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class Mail {
	public  void sendmail(String TO,String SUBJECT,String TEXT  ) {
    
   
		// String HOST = "smtp.mail.yahoo.com";
		String HOST = "smtp.gmail.com";
		String USER = "mynkd15@gmail.com";
		String PASSWORD = "Aakhiraat@15989"; // sender mail password
		String PORT = "465";
		String FROM = "mynkd15@gmail.com"; // mail id
		//String TO = "mynkd15@gmail.com"; // receiver mail id

		String STARTTLS = "true";
		String AUTH = "true";
		String DEBUG = "true";
		String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
		//String SUBJECT = "u can recover ur password from this link";

		// Use Properties object to set environment properties
		Properties props = new Properties();

		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.user", USER);

		props.put("mail.smtp.auth", AUTH);
		props.put("mail.smtp.starttls.enable", STARTTLS);
		props.put("mail.smtp.debug", DEBUG);

		props.put("mail.smtp.socketFactory.port", PORT);
		props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		try {
			// Obtain the default mail session
			Session session1 = Session.getDefaultInstance(props, null);
			session1.setDebug(true);

			// Construct the mail message
			MimeMessage message = new MimeMessage(session1);
			//message.setText("hello I am again inside seminaro test2 ");
			message.setText(TEXT);
			message.setSubject(SUBJECT);
			message.setFrom(new InternetAddress(FROM));
			message.addRecipient(RecipientType.TO, new InternetAddress(TO));
			message.saveChanges();

			// Use Transport to deliver the message
			Transport transport = session1.getTransport("smtp");
			transport.connect(HOST, USER, PASSWORD);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			System.out.println("Mail send");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
