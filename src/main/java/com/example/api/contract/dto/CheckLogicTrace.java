package com.example.api.contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CheckLogicTrace {
    @JsonProperty("matched_condition_id")
    private Long matchedConditionId;
    @JsonProperty("logic_id")
    private String logicId;
    @JsonProperty("logic_desc")
    private String logicDesc;
    @JsonProperty("logic_class")
    private String logicClass;
    @JsonProperty("logic_methd")
    private String logicMethod;
    @JsonProperty("call_start")
    private LocalDateTime callStart;
    @JsonProperty("call_end")
    private LocalDateTime callEnd;
}
