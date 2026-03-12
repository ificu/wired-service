package com.example.api.infra;

import com.example.api.domain.CheckConditionComposite;
import com.example.api.domain.CheckLogic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CheckConditionMapper {
    List<CheckConditionComposite> findAllConditions();

    /** comp_id에 해당하는 체크 로직 목록을 order_seq 순으로 조회 */
    List<CheckLogic> findLogicsByCompId(Long compId);
}
