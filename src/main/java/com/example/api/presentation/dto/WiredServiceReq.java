package com.example.api.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WiredServiceReq {
    // Data 항목
    private String serviceType;
    private String serviceName;
    private String serviceNumber;
    private String customerId;
    private String contractNumber;
    // 동적 조건 체크 추가용
    private String serviceCode;    // 서비스구분
    private String serviceDetailCode; // 서비스상세구분
    private String serviceUsageCategory; // 서비스이용종류
    private String changeCode;    // 변경유형
}
