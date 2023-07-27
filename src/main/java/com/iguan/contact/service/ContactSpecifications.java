package com.iguan.contact.service;

import com.iguan.contact.entity.Contact;
import org.springframework.data.jpa.domain.Specification;

public class ContactSpecifications {
    public static Specification<Contact> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("phoneNumbers"), phoneNumber);
    }

    public static Specification<Contact> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("emails"), email);
    }

    public static Specification<Contact> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}

