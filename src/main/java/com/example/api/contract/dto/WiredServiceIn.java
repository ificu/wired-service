package com.example.api.contract.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WiredServiceIn {
    private String serviceType;
    private String serviceName;
    private String serviceNumber;
    private String customerId;
    private String contractNumber;
    @ConditionAttr(column = "svc_cd")
    private String serviceCode;    // 서비스구분
    @ConditionAttr(column = "svc_dtl_cd")
    private String serviceDetailCode; // 서비스상세구분
    @ConditionAttr(column = "svc_use_ctg")
    private String serviceUsageCategory; // 서비스이용종류
    @ConditionAttr(column = "chg_cd")
    private String changeCode;    // 변경유형
}
