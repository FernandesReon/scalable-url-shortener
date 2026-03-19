package com.reon.userservice.service;

import com.reon.userservice.dto.RegistrationRequest;
import com.reon.userservice.dto.response.RegistrationResponse;
import com.reon.userservice.dto.response.UserProfile;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest registrationRequest);

    UserProfile fetchUserProfile(String userId);

    void incrementUrlCountForUser(String userId);
    void decrementUrlCountForUser(String userId);
}
