package com.example.api.application.checklogic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 고객 특성 제약 조건
@Slf4j
@Component
public class CustProfileConstraint {

    // 개인정보유출금지고객
    public void checkPrivacyLeakRestrictedCustomer(String serviceNumber) {
        log.info("=================================");
        log.info("개인정보유출금지고객 체크 로직 수행");
        log.info("=================================");
    }

    // 소상공 혜택 등록 가능건물
    public void checkSmallBusinessBenefitBuildingEligibility(String serviceNumber) {
        log.info("=================================");
        log.info("소상공 혜택 등록 가능건물 체크 로직 수행");
        log.info("=================================");        
    }

}
