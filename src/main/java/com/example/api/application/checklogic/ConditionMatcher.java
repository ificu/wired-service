package com.example.api.application.checklogic;

import com.example.api.contract.dto.WiredServiceIn;
import com.example.api.domain.CheckConditionComposite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DB에서 조회한 조건 목록과 입력값을 비교하여 매칭되는 comp_id 목록을 반환
 *
 * 매칭 규칙:
 *  - 같은 comp_id 내 모든 cond_attr_cd 조건을 AND 로 평가
 *  - 단일 cond_attr_cd 의 여러 값(comma-separated)은 OR 로 평가
 */
public class ConditionMatcher {

    private ConditionMatcher() {}

    /**
     * @param conditions DB 조회 결과 (전체 조건 행)
     * @param in         입력 DTO
     * @return 조건을 모두 만족하는 comp_id 목록 (오름차순)
     */
    public static List<Long> match(List<CheckConditionComposite> conditions, WiredServiceIn in) {
        // comp_id -> (cond_attr_cd -> allowed values set)
        Map<Long, Map<String, Set<String>>> grouped = conditions.stream()
                .collect(Collectors.groupingBy(
                        CheckConditionComposite::getCompId,
                        Collectors.toMap(
                                CheckConditionComposite::getCondAttrCd,
                                c -> Arrays.stream(c.getCondAttrVal().split(","))
                                           .map(String::trim)
                                           .collect(Collectors.toSet())
                        )
                ));

        Map<String, String> inputMap = toInputMap(in);

        return grouped.entrySet().stream()
                .filter(entry -> allMatch(entry.getValue(), inputMap))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    /** 조건 조합 내 모든 항목이 입력값과 매칭되는지 (AND) */
    private static boolean allMatch(Map<String, Set<String>> condMap, Map<String, String> inputMap) {
        return condMap.entrySet().stream()
                .allMatch(e -> {
                    String inputVal = inputMap.get(e.getKey());
                    return inputVal != null && e.getValue().contains(inputVal);
                });
    }

    /** WiredServiceIn 의 @ConditionAttr 필드를 리플렉션으로 읽어 Map 으로 변환
     *  키: @ConditionAttr(column = "...") 에 명시된 DB 컬럼명
     */
    private static Map<String, String> toInputMap(WiredServiceIn in) {
        Map<String, String> map = new java.util.HashMap<>();
        for (java.lang.reflect.Field field : in.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(com.example.api.contract.dto.ConditionAttr.class)) continue;
            field.setAccessible(true);
            try {
                Object value = field.get(in);
                if (value == null) continue;
                String key = field.getAnnotation(com.example.api.contract.dto.ConditionAttr.class).column();
                map.put(key, value.toString());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("ConditionAttr 필드 접근 실패: " + field.getName(), e);
            }
        }
        return map;
    }
}
