package com.reon.userservice.service.impl;

import com.reon.exception.EmailAlreadyExistsException;
import com.reon.exception.UserNotFoundException;
import com.reon.userservice.dto.RegistrationRequest;
import com.reon.userservice.dto.response.RegistrationResponse;
import com.reon.userservice.dto.response.UserProfile;
import com.reon.userservice.mapper.UserMapper;
import com.reon.userservice.model.User;
import com.reon.userservice.model.type.AuthProvider;
import com.reon.userservice.model.type.Role;
import com.reon.userservice.model.type.Tier;
import com.reon.userservice.repository.UserRepository;
import com.reon.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.email())) {
            log.warn("User Service :: User already exists");
            throw new EmailAlreadyExistsException("User already exists with this email");
        }

        User user = userMapper.mapToEntity(registrationRequest);
        user.setPassword(encoder.encode(registrationRequest.password()));
        user.setTier(Tier.FREE);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setRole(EnumSet.of(Role.USER));
        user.setEmailVerified(true);

        User saveUser = userRepository.save(user);
        return userMapper.mapToResponse(saveUser);
    }

    @Override
    public UserProfile fetchUserProfile(String userId) {
        return null;
    }

    @Override
    @Transactional
    public void incrementUrlCountForUser(String userId) {
        User user = findIfUserIsActive(userId);
        if (user != null){
            userRepository.incrementUrlCount(user.getUserId());
        }
    }

    @Override
    public void decrementUrlCountForUser(String userId) {
        User user = findIfUserIsActive(userId);
        if (user != null){
            userRepository.decrementUserUrlCount(user.getUserId());
        }
    }

    private User findIfUserIsActive(String userId) {
        return userRepository.findById(userId)
                .filter(User::isActive)
                .orElse(null);
    }
}
