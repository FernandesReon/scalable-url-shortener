package com.reon.userservice.dto.response;

import com.reon.userservice.model.type.Tier;
import lombok.Builder;

@Builder
public record UserProfile(
        String userId,
        String name,
        String email,
        Tier tier,
        int urlsCreated,
        Integer urlCreationLimit
) {
}
