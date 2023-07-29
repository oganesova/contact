package com.iguan.contact.controller;

import com.iguan.contact.entity.Email;
import com.iguan.contact.entity.PhoneNumber;
import com.iguan.contact.service.PhoneNumberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/numbers")
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }
    @GetMapping("/list")
    public ResponseEntity<List<PhoneNumber>> getAllNumbers() {
        List<PhoneNumber> numbers = phoneNumberService.getAllNumbers();
        return ResponseEntity.ok(numbers);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PhoneNumber> getNumbersById(@PathVariable Long id) {
        PhoneNumber phoneNumber = phoneNumberService.getNumberById(id);
        if (phoneNumber != null) {
            return ResponseEntity.ok(phoneNumber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/")
    public ResponseEntity<PhoneNumber> createNumber(@RequestBody PhoneNumber phoneNumber) {
        PhoneNumber savedNumber = phoneNumberService.saveNumber(phoneNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNumber);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PhoneNumber> updateNumber(@PathVariable Long id, @RequestBody PhoneNumber updatedNumber) {
        PhoneNumber existingNumber = phoneNumberService.getNumberById(id);
        if (existingNumber != null) {
            updatedNumber.setId(id);
            PhoneNumber savedNumber = phoneNumberService.saveNumber(updatedNumber);
            return ResponseEntity.ok(savedNumber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/soft/{phoneNumberId}")
    public ResponseEntity<String> softDeletePhoneNumber(@PathVariable Long phoneNumberId) {
        phoneNumberService.softDeletePhoneNumberById(phoneNumberId);
        return ResponseEntity.ok("Phone number soft deleted successfully.");
    }

    @DeleteMapping("/{id}")
    public void deleteNumber(@PathVariable Long id) {
        phoneNumberService.deleteNumberById(id);
    }
}
