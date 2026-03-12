package com.example.api.application.checklogic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 가입 대리점 제약 조건
@Slf4j
@Component
public class SubscrpAgencyConstraint {

    // 동일장소해지후가입불가
    public void checkSameLocationTerminationRestriction(String serviceNumber) {
        log.info("=================================");
        log.info("동일장소해지후가입불가 체크 로직 수행");
        log.info("=================================");
    }

    // 동일장소인터넷tv 제한
    public void checkSameLocationInternetTvRestriction(String serviceNumber) {
        log.info("=================================");
        log.info("동일장소인터넷tv 제한 체크 로직 수행");
        log.info("=================================");
    }

}
