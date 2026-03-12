package com.example.api.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 체크 조건 조합 - comp_id 하나에 속한 조건 항목 목록
 */
@Getter
@Setter
public class CheckConditionComposite {
    private Long compId;
    private String condAttrCd;
    private String condAttrVal; // comma-separated (e.g. "C8,C6")
}
