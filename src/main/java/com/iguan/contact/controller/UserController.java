package com.iguan.contact.controller;

import com.iguan.contact.config.AuthRequest;
import com.iguan.contact.config.AuthResponse;
import com.iguan.contact.config.JwtTokenUtil;
import com.iguan.contact.entity.Email;
import com.iguan.contact.entity.PhoneNumber;
import com.iguan.contact.entity.User;
import com.iguan.contact.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        userService.registerUser(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    @PatchMapping("/{userId}/email")
    public ResponseEntity<User> updateUserEmail(@PathVariable Long userId, @RequestBody Email updatedEmail) {
        User user = userService.updateUserEmail(userId, updatedEmail);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{userId}/phone")
    public ResponseEntity<User> updateUserPhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumber updatedPhoneNumber) {
        User user = userService.updateUserPhoneNumber(userId, updatedPhoneNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/phone")
    public ResponseEntity<User> addPhoneNumberToUser(@PathVariable Long userId, @RequestBody PhoneNumber newNumber) {
        User updatedUser = userService.addPhoneNumberToUser(userId, newNumber);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/email")
    public ResponseEntity<User> addEmailToUser(@PathVariable Long userId, @RequestBody Email newEmail) {
        User updatedUser = userService.addEmailToUser(userId, newEmail);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<User>> getUsersByPage(@PageableDefault(size = 10) Pageable pageable) {
        Page<User> usersPage = userService.getUsersByPage(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUserById(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/email")
    public ResponseEntity<User> updateUserEmailById(@PathVariable Long userId, @RequestBody Email updatedEmail) {
        User user = userService.updateUserEmail(userId, updatedEmail);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/phone")
    public ResponseEntity<User> updateUserPhoneNumberById(@PathVariable Long userId, @RequestBody PhoneNumber updatedPhoneNumber) {
        User user = userService.updateUserPhoneNumber(userId, updatedPhoneNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
        userService.softDeleteUserById(userId);
        return ResponseEntity.ok("User soft deleted successfully.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
