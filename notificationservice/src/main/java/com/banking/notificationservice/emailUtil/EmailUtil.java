package com.banking.notificationservice.emailUtil;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.banking.notificationservice.dto.TransactionEmailRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public String accountCreationMail(String email) throws MessagingException, UnsupportedEncodingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
	    mimeMessageHelper.setFrom("sricharan824@gmail.com", "SBI Bank");
	    mimeMessageHelper.setTo(email);
	    mimeMessageHelper.setSubject("Your SBI Bank Account Has Been Created Successfully!");

	    String body = String.format(
	        "<html>" +
	        "<head>" +
	        "<style>" +
	        "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
	        ".container { max-width: 600px; background: #ffffff; margin: 20px auto; padding: 20px; border-radius: 8px; " +
	        "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
	        "h1 { color: #1b5e20; text-align: center; }" +
	        "p { font-size: 16px; color: #333; }" +
	        ".details { background: #f9f9f9; padding: 10px; border-radius: 5px; }" +
	        ".footer { text-align: center; padding-top: 10px; font-size: 14px; color: #777; }" +
	        "strong { color: #333; }" +
	        "</style>" +
	        "</head>" +
	        "<body>" +
	        "<div class='container'>" +
	        "<h1>Welcome to SBI Bank!</h1>" +
	        "<p>Dear Customer,</p>" +
	        "<p>Congratulations! Your SBI Bank account has been successfully created.</p>" +
	        "<div class='details'>" +
	        "<p><strong>Registered Email:</strong> " + email + "</p>" +
	        "</div>" +
	        "<p>You can now enjoy seamless banking services, secure transactions, and exclusive benefits.</p>" +
	        "<p>For any queries or support, feel free to contact our customer care.</p>" +
	        "<p class='footer'>Thank you for choosing SBI Bank.<br><strong>The SBI Bank Team</strong></p>" +
	        "</div>" +
	        "</body>" +
	        "</html>");

	    mimeMessageHelper.setText(body, true);
	    javaMailSender.send(mimeMessage);

	    return "Mail Sent Successfully..!!";
	}

	
	public void sendTransactionEmail(TransactionEmailRequest request)
	        throws MessagingException, UnsupportedEncodingException {

	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

	    helper.setFrom("sricharan824@gmail.com", "SBI Bank");
	    helper.setTo(request.getToEmail());

	    String type = request.getTransactionType().toUpperCase();
	    
	    // Setting the email subject based on transaction type
	    String subject;
	    if (type.equals("DEPOSIT")) {
	        subject = "Deposit Alert - SBI Bank";
	    } else if (type.equals("WITHDRAW")) {
	        subject = "Withdrawal Alert - SBI Bank";
	    } else if (type.equals("TRANSFER")) {
	        subject = "Transfer Alert - SBI Bank";
	    } else {
	        subject = "Transaction Alert - SBI Bank";
	    }
	    helper.setSubject(subject);

	    // Setting the email body message
	    String transactionMessage;
	    if (type.equals("DEPOSIT")) {
	        transactionMessage = "deposited into your account.";
	    } else if (type.equals("WITHDRAW")) {
	        transactionMessage = "withdrawn from your account.";
	    } else if (type.equals("TRANSFER")) {
	        transactionMessage = "transferred from your account.";
	    } else {
	        transactionMessage = "processed in your account.";
	    }

	    String body = String.format(
	        "<html><head><style>" +
	        "body { font-family: Arial; background-color: #f4f4f4; padding: 0; }" +
	        ".container { max-width: 600px; background: #fff; margin: 20px auto; padding: 20px; border-radius: 8px; " +
	        "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
	        "h1 { color: #1b5e20; text-align: center; }" +
	        "p { font-size: 16px; color: #333; }" +
	        ".footer { text-align: center; padding-top: 10px; font-size: 14px; color: #777; }" +
	        "</style></head><body>" +
	        "<div class='container'>" +
	        "<h1>%s Confirmation - SBI Bank</h1>" +
	        "<p>Dear Customer,</p>" +
	        "<p>We would like to inform you that ₹<strong>%.2f</strong> has been %s</p>" +
	        "<p><strong>Email:</strong> %s</p>" +
	        "<p><strong>Date & Time:</strong> %s</p>" +
	        "<p class='footer'>Thank you for banking with us.<br><strong>The SBI Bank Team</strong></p>" +
	        "</div></body></html>",
	        type, request.getAmount(), transactionMessage,
	        request.getToEmail(),
	        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
	    );

	    helper.setText(body, true);
	    javaMailSender.send(mimeMessage);
	}


}
