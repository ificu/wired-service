package com.example.api.contract.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WiredServiceOut {
    private Long id;
    private String serviceType;
    private String serviceName;
    private String serviceNumber;
    private String customerId;
    private String contractNumber;
    private ExecutionTrace trace;
}
