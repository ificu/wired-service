package com.example.api.presentation;

import com.example.api.contract.WiredServiceService;
import com.example.api.presentation.dto.WiredServiceReq;
import com.example.api.presentation.dto.WiredServiceRes;
import com.example.api.presentation.mapper.WiredServicePresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wired-services")
@RequiredArgsConstructor
public class WiredServiceController {

    private final WiredServiceService wiredServiceService;

    @GetMapping
    public List<WiredServiceRes> getAll() {
        return wiredServiceService.findAll().stream()
                .map(WiredServicePresentationMapper::toRes)
                .toList();
    }

    @GetMapping("/{id}")
    public WiredServiceRes getById(@PathVariable Long id) {
        return WiredServicePresentationMapper.toRes(wiredServiceService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WiredServiceRes create(@RequestBody WiredServiceReq request) {
        return WiredServicePresentationMapper.toRes(wiredServiceService.create(WiredServicePresentationMapper.toIn(request)));
    }

    @PutMapping("/{id}")
    public WiredServiceRes update(@PathVariable Long id, @RequestBody WiredServiceReq request) {
        return WiredServicePresentationMapper.toRes(wiredServiceService.update(id, WiredServicePresentationMapper.toIn(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        wiredServiceService.delete(id);
    }
}
