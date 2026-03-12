package com.example.api.contract;

import com.example.api.contract.dto.WiredServiceIn;
import com.example.api.contract.dto.WiredServiceOut;

import java.util.List;

public interface WiredServiceService {
    List<WiredServiceOut> findAll();
    WiredServiceOut findById(Long id);
    WiredServiceOut create(WiredServiceIn in);
    WiredServiceOut update(Long id, WiredServiceIn in);
    void delete(Long id);
}
