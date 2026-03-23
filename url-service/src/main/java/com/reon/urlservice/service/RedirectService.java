package com.reon.urlservice.service;

import com.reon.urlservice.dto.RedirectRequest;
import com.reon.urlservice.dto.response.UrlResponse;

public interface RedirectService {
    UrlResponse redirectUserToOriginalUrl(RedirectRequest redirectRequest);
}
