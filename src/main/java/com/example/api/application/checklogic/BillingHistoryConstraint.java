package com.example.api.application.checklogic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 납부 내역 제약 조건
@Slf4j
@Component
public class BillingHistoryConstraint {

    // SKB미납금존재
    public void checkUnpaidBalance(String serviceNumber) {
        log.info("=================================");
        log.info("SKB미납금존재 체크 로직 수행");
        log.info("=================================");

    }

    // 미환급금 안내
    public void checkUnclaimedRefund(String serviceNumber) {
        log.info("=================================");
        log.info("미환급금 안내 체크 로직 수행");
        log.info("=================================");        

    }
}
