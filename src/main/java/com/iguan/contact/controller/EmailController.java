package com.iguan.contact.controller;

import com.iguan.contact.entity.Email;
import com.iguan.contact.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Email>> getAllEmails() {
        List<Email> emails = emailService.getAllEmails();
        return ResponseEntity.ok(emails);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Email email = emailService.getEmailById(id);
        if (email != null) {
            return ResponseEntity.ok(email);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/")
    public ResponseEntity<Email> createEmail(@RequestBody Email email) {
        Email savedEmail = emailService.saveEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmail);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody Email updatedEmail) {
        Email existingEmail = emailService.getEmailById(id);
        if (existingEmail != null) {
            updatedEmail.setId(id);
            Email savedEmail = emailService.saveEmail(updatedEmail);
            return ResponseEntity.ok(savedEmail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{emailId}")
    public ResponseEntity<String> softDeleteEmail(@PathVariable Long emailId) {
        emailService.softDeleteEmailById(emailId);

        return ResponseEntity.ok("Email soft deleted successfully.");
    }
    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable Long id) {
        emailService.deleteEmailById(id);
    }
}

