package com.iguan.contact.service;

import com.iguan.contact.entity.Email;
import com.iguan.contact.entity.PhoneNumber;
import com.iguan.contact.entity.User;
import com.iguan.contact.repository.EmailRepository;
import com.iguan.contact.repository.PhoneNumberRepository;
import com.iguan.contact.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EmailRepository emailRepository, PhoneNumberRepository phoneNumberRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User addPhoneNumberToUser(Long userId, PhoneNumber newNumber) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            newNumber.setUser(user);
            user.getPhoneNumbers().add(newNumber);
            return userRepository.save(user);
        }
        return null;
    }

    public User addEmailToUser(Long userId, Email newEmail) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            newEmail.setUser(user);
            user.getEmails().add(newEmail);
            return userRepository.save(user);
        }
        return null;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User softDeleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setDeleted(true);
        return userRepository.save(user);
    }

    public Page<User> getUsersByPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUserById(Long userId, User updatedUser) {
        User user = getUserById(userId);
        if (user != null) {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setAge(updatedUser.getAge());
            user.setPhoneNumbers(updatedUser.getPhoneNumbers());
            user.setEmails(updatedUser.getEmails());
            return userRepository.save(user);
        }
        return null;
    }


    public User updateUserEmail(Long userId, Email updatedEmail) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Email existingEmail = user.getEmails()
                    .stream()
                    .filter(email -> email.getId().equals(updatedEmail.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingEmail != null) {
                existingEmail.setEmail(updatedEmail.getEmail());
                existingEmail.setDomainName(updatedEmail.getDomainName());
                return userRepository.save(user);
            }
        }
        return null;
    }

    public User updateUserPhoneNumber(Long userId, PhoneNumber updatedPhoneNumber) {
        User user = getUserById(userId);
        if (user != null) {
            PhoneNumber existingNumber = user.getPhoneNumbers().stream()
                    .filter(phoneNumber -> phoneNumber.getId().equals(updatedPhoneNumber.getId())).findFirst()
                    .orElse(null);
            if (existingNumber != null) {
                existingNumber.setPhoneNumber(updatedPhoneNumber.getPhoneNumber());
                existingNumber.setCountry(updatedPhoneNumber.getCountry());
                existingNumber.setLabel(updatedPhoneNumber.getLabel());
            }
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}

