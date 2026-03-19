package com.reon.userservice.dto.response;

import com.reon.userservice.model.type.Tier;
import lombok.Builder;

@Builder
public record RegistrationResponse(
        String userId,
        String email,
        Tier tier
) {
}