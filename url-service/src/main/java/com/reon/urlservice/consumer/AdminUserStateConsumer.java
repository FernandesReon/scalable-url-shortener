package com.reon.urlservice.consumer;

import com.reon.events.AdminUserStateControlEvent;
import com.reon.urlservice.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AdminUserStateConsumer {
    private final Logger log = LoggerFactory.getLogger(AdminUserStateConsumer.class);
    private final UrlService urlService;

    public AdminUserStateConsumer(UrlService urlService) {
        this.urlService = urlService;
    }

    @KafkaListener(
            topics = "user.state",
            groupId = "url-service-group"
    )
    public void consumeAdminEvent(AdminUserStateControlEvent adminEvent) {
        log.info("URL Service :: Consuming Admin User State Event");
        log.info("URL Service :: Event consumed.");
    }
}
