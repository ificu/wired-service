package com.example.api.presentation.dto;

import com.example.api.contract.dto.ExecutionTrace;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WiredServiceRes {
    private Long id;
    private String serviceType;
    private String serviceName;
    private String serviceNumber;
    private String customerId;
    private String contractNumber;
    private ExecutionTrace trace;
}
