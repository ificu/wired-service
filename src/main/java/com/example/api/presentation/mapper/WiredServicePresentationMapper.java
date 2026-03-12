package com.example.api.presentation.mapper;

import com.example.api.contract.dto.WiredServiceIn;
import com.example.api.contract.dto.WiredServiceOut;
import com.example.api.presentation.dto.WiredServiceReq;
import com.example.api.presentation.dto.WiredServiceRes;

public class WiredServicePresentationMapper {

    private WiredServicePresentationMapper() {}

    public static WiredServiceIn toIn(WiredServiceReq req) {
        return WiredServiceIn.builder()
                .serviceType(req.getServiceType())
                .serviceName(req.getServiceName())
                .serviceNumber(req.getServiceNumber())
                .customerId(req.getCustomerId())
                .contractNumber(req.getContractNumber())
                .serviceCode(req.getServiceCode())
                .serviceDetailCode(req.getServiceDetailCode())
                .serviceUsageCategory(req.getServiceUsageCategory())
                .changeCode(req.getChangeCode())
                .build();
    }

    public static WiredServiceRes toRes(WiredServiceOut out) {
        return WiredServiceRes.builder()
                .id(out.getId())
                .serviceType(out.getServiceType())
                .serviceName(out.getServiceName())
                .serviceNumber(out.getServiceNumber())
                .customerId(out.getCustomerId())
                .contractNumber(out.getContractNumber())
                .trace(out.getTrace())
                .build();
    }
}
