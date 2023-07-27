package com.iguan.contact.service;

import com.iguan.contact.entity.Email;
import com.iguan.contact.repository.EmailRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class EmailService {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

        public List<Email> getAllEmails() {
            return emailRepository.findAll();
        }

        public Email getEmailById(Long id) {
            return emailRepository.findById(id).orElse(null);
        }

        public Email saveEmail(Email email) {
            return emailRepository.save(email);
        }

        public void deleteEmailById(Long id) {
            emailRepository.deleteById(id);
        }
    public Email updateEmail(Email updatedEmail) {
        if (updatedEmail.getId() == null || !emailRepository.existsById(updatedEmail.getId())) {
            throw new IllegalArgumentException("Invalid email ID");
        }
        return emailRepository.save(updatedEmail);
    }
    public Email softDeleteEmailById(Long emailId) {
        Email email = emailRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        email.setDeleted(true);
        return emailRepository.save(email);
    }
    }

