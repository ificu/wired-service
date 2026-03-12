package com.example.api.contract.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 동적 조건 체크 대상 필드 마커 어노테이션
 * column: DB의 cond_attr_cd 값과 매핑되는 컬럼명 (필수)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionAttr {
    String column();
}
