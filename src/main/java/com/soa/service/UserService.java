package com.soa.service;

import com.soa.model.Role;
import com.soa.model.User;
import com.soa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    public User registerUser(String email, String rawPassword) {
        if (Objects.nonNull(userRepository.findByEmail(email))) {
            throw new IllegalArgumentException("Email già registrata");
        }

        String salt = generateSalt();
        String hash = DigestUtils.sha1Hex(rawPassword + salt);

        User user = new User();
        user.setEmail(email);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    private String generateSalt() {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
