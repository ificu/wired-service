package com.example.api.infra;

import com.example.api.domain.WiredServiceQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface WiredServiceMapper {
    List<WiredServiceQuery> findAll();
    Optional<WiredServiceQuery> findById(Long id);
    void insert(WiredServiceQuery item);
    void update(WiredServiceQuery item);
    void deleteById(Long id);
    boolean existsById(Long id);
}
