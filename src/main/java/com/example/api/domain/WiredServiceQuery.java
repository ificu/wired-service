package com.example.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WiredServiceQuery {
    private Long id;
    private String serviceType;
    private String serviceName;
    private String serviceNumber;
    private String customerId;
    private String contractNumber;
}
