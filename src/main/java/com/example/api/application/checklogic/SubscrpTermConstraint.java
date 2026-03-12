package com.example.api.application.checklogic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 가입 기간 제약 조건
@Slf4j
@Component
public class SubscrpTermConstraint {

    // 3개월내해지가입제한
    public void checkReSubscriptionWithinThreeMonths(String serviceNumber) {
        log.info("=================================");
        log.info("3개월내해지가입제한");
        log.info("=================================");
    }

}
