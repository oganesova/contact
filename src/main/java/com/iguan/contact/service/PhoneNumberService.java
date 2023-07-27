package com.iguan.contact.service;

import com.iguan.contact.entity.Email;
import com.iguan.contact.entity.PhoneNumber;
import com.iguan.contact.repository.PhoneNumberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public List<PhoneNumber> getAllNumbers() {
        return phoneNumberRepository.findAll();
    }

    public PhoneNumber getNumberById(Long id) {
        return phoneNumberRepository.findById(id).orElse(null);
    }

    public PhoneNumber saveNumber(PhoneNumber phoneNumber) {
        return phoneNumberRepository.save(phoneNumber);
    }

    public void deleteNumberById(Long id) {
        phoneNumberRepository.deleteById(id);
    }
    public PhoneNumber updateNumber(PhoneNumber updatedNumber) {
        if (updatedNumber.getId() == null || !phoneNumberRepository.existsById(updatedNumber.getId())) {
            throw new IllegalArgumentException("Invalid Number ID");
        }
        return phoneNumberRepository.save(updatedNumber);
    }
    public PhoneNumber softDeletePhoneNumberById(Long phoneNumberId) {
        PhoneNumber phoneNumber = phoneNumberRepository.findById(phoneNumberId)
                .orElseThrow(() -> new EntityNotFoundException("Phone number not found"));

        phoneNumber.setDeleted(true);
        return phoneNumberRepository.save(phoneNumber);
    }
}
