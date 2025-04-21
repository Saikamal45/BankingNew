package com.banking.notificationservice.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.banking.notificationservice.dto.TransactionEmailRequest;
import com.banking.notificationservice.emailUtil.EmailUtil;

import jakarta.mail.MessagingException;

@RestController
public class EmailController {

	@Autowired
	private EmailUtil emailUtil;
	
	@PostMapping("/sendAccountCreationMail")
	public ResponseEntity<String> accountCreationMail(@RequestParam String email) throws UnsupportedEncodingException, MessagingException{
		String accountCreationMail = emailUtil.accountCreationMail(email);
		return new ResponseEntity<String>(accountCreationMail,HttpStatus.OK);
	}
	
	@PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody TransactionEmailRequest request) {
        try {
            emailUtil.sendTransactionEmail(request);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email sending failed!");
        }
    }
}
