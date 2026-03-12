package com.example.api.application.checklogic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 회선 한도 제약 조건
@Slf4j
@Component
public class LineLimitConstraint {

    // 유선가입회선 한도
    public void checkFixedLineSubscriptionLimit(String serviceNumber) {
        log.info("=================================");
        log.info("유선가입회선 한도 체크 로직 수행");
        log.info("=================================");
    }

    // 타사미납
    public void checkOtherCarrierUnpaidBalance(String serviceNumber) {
        log.info("=================================");
        log.info("타사미납 체크 로직 수행");
        log.info("=================================");
    }

    // 신용가입제한체크
    public void checkCreditSubscriptionRestriction(String serviceNumber) {
        log.info("=================================");
        log.info("신용가입제한체크 체크 로직 수행");
        log.info("=================================");
    }

}
