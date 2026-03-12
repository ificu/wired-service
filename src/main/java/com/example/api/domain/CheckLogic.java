package com.example.api.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 체크 로직 Pool - 수행할 로직 정보
 */
@Getter
@Setter
public class CheckLogic {
    private String logicId;
    private String logicDesc;
    private String logicClass;
    private String logicMethod;
}
