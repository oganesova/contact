package com.iguan.contact.controller;

import com.iguan.contact.entity.Contact;
import com.iguan.contact.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    @GetMapping("/search")
    public ResponseEntity<List<Contact>> searchContacts(
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name
    ) {
        List<Contact> contacts = contactService.searchContacts(phoneNumber, email, name);
        return ResponseEntity.ok(contacts);
    }
    @DeleteMapping("/{contactId}")
    public ResponseEntity<String> softDeleteContact(@PathVariable Long contactId) {
        contactService.softDeleteContactById(contactId);
        return ResponseEntity.ok("Contact soft deleted successfully.");
    }
}