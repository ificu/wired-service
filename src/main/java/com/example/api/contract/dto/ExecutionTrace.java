package com.example.api.contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExecutionTrace {
    @JsonProperty("matched_condition")
    private List<Long> matchedConditions;
    @JsonProperty("check_logic_trace")
    private List<CheckLogicTrace> checkLogicTrace;
}
