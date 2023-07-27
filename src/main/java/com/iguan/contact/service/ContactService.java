package com.iguan.contact.service;

import com.iguan.contact.entity.Contact;
import com.iguan.contact.repository.ContactRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> searchContacts(String phoneNumber, String email, String name) {
        return contactRepository.findAll(
                (root, query, criteriaBuilder) -> {
                    Specification<Contact> spec = Specification.where(null);

                    if (phoneNumber != null && !phoneNumber.isEmpty()) {
                        spec = spec.and(ContactSpecifications.hasPhoneNumber(phoneNumber));
                    }

                    if (email != null && !email.isEmpty()) {
                        spec = spec.and(ContactSpecifications.hasEmail(email));
                    }

                    if (name != null && !name.isEmpty()) {
                        spec = spec.and(ContactSpecifications.hasName(name));
                    }

                    return spec.toPredicate(root, query, criteriaBuilder);
                }
        );
    }

    public Contact softDeleteContactById(Long contactId) {


        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() ->

                        new EntityNotFoundException("Contact not found"));

        contact.setDeleted(true);


        return contactRepository.save(contact);
    }
}
