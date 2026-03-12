package com.example.api.application;

import com.example.api.application.checklogic.ConditionMatcher;
import com.example.api.contract.WiredServiceService;
import com.example.api.contract.dto.CheckLogicTrace;
import com.example.api.contract.dto.ExecutionTrace;
import com.example.api.contract.dto.WiredServiceIn;
import com.example.api.contract.dto.WiredServiceOut;
import com.example.api.domain.CheckConditionComposite;
import com.example.api.domain.CheckLogic;
import com.example.api.domain.WiredServiceQuery;
import com.example.api.domain.exception.WiredServiceNotFoundException;
import com.example.api.infra.CheckConditionMapper;
import com.example.api.infra.WiredServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WiredServiceServiceImpl implements WiredServiceService {

    private final WiredServiceMapper wiredServiceMapper;
    private final CheckConditionMapper checkConditionMapper;
    private final ApplicationContext applicationContext;

    @Override
    public List<WiredServiceOut> findAll() {
        return wiredServiceMapper.findAll().stream()
                .map(this::toOut)
                .toList();
    }

    @Override
    public WiredServiceOut findById(Long id) {
        return wiredServiceMapper.findById(id)
                .map(this::toOut)
                .orElseThrow(() -> new WiredServiceNotFoundException(id));
    }

    @Override
    public WiredServiceOut create(WiredServiceIn in) {
        // 1. DB에서 전체 조건 조합 조회
        List<CheckConditionComposite> conditions = checkConditionMapper.findAllConditions();

        // 2. 입력값과 매칭되는 comp_id 목록 추출
        List<Long> matchedCompIds = ConditionMatcher.match(conditions, in);
        log.info("매칭된 체크 조건 comp_id: {}", matchedCompIds);

        // 3. 매칭된 comp_id 별 체크 로직 수행 및 trace 수집
        List<CheckLogicTrace> logicTraces = new ArrayList<>();
        for (Long compId : matchedCompIds) {
            List<CheckLogic> logics = checkConditionMapper.findLogicsByCompId(compId);
            for (CheckLogic logic : logics) {
                log.info("COMP_ID {} - 로직 수행: [{}] {}.{}",
                        compId, logic.getLogicId(), logic.getLogicClass(), logic.getLogicMethod());
                logicTraces.add(invokeCheckLogic(compId, logic, in.getServiceNumber()));
            }
        }

        // 4. 이상이 없으면 아래 실행 실제 업무처리 로직 수행
        WiredServiceQuery item = WiredServiceQuery.builder()
                .serviceType(in.getServiceType())
                .serviceName(in.getServiceName())
                .serviceNumber(in.getServiceNumber())
                .customerId(in.getCustomerId())
                .contractNumber(in.getContractNumber())
                .build();
        wiredServiceMapper.insert(item);

        return toOut(item, ExecutionTrace.builder()
                .matchedConditions(matchedCompIds)
                .checkLogicTrace(logicTraces)
                .build());
    }

    @Override
    public WiredServiceOut update(Long id, WiredServiceIn in) {
        if (!wiredServiceMapper.existsById(id)) {
            throw new WiredServiceNotFoundException(id);
        }
        WiredServiceQuery item = WiredServiceQuery.builder()
                .id(id)
                .serviceType(in.getServiceType())
                .serviceName(in.getServiceName())
                .serviceNumber(in.getServiceNumber())
                .customerId(in.getCustomerId())
                .contractNumber(in.getContractNumber())
                .build();
        wiredServiceMapper.update(item);
        return toOut(item);
    }

    @Override
    public void delete(Long id) {
        if (!wiredServiceMapper.existsById(id)) {
            throw new WiredServiceNotFoundException(id);
        }
        wiredServiceMapper.deleteById(id);
    }

    private WiredServiceOut toOut(WiredServiceQuery q) {
        return toOut(q, null);
    }

    private WiredServiceOut toOut(WiredServiceQuery q, ExecutionTrace trace) {
        return WiredServiceOut.builder()
                .id(q.getId())
                .serviceType(q.getServiceType())
                .serviceName(q.getServiceName())
                .serviceNumber(q.getServiceNumber())
                .customerId(q.getCustomerId())
                .contractNumber(q.getContractNumber())
                .trace(trace)
                .build();
    }

    /**
     * ApplicationContext에서 logic_class 빈을 꺼내 logic_methd를 호출하고 trace 반환
     */
    private CheckLogicTrace invokeCheckLogic(Long compId, CheckLogic logic, String serviceNumber) {
        String fqcn = "com.example.api.application.checklogic." + logic.getLogicClass();
        LocalDateTime callStart = LocalDateTime.now();
        try {
            Class<?> clazz = Class.forName(fqcn);
            Object bean = applicationContext.getBean(clazz);
            Method method = clazz.getMethod(logic.getLogicMethod(), String.class);
            method.invoke(bean, serviceNumber);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("체크 로직 클래스를 찾을 수 없습니다: " + fqcn, e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("체크 로직 메서드를 찾을 수 없습니다: " + logic.getLogicMethod(), e);
        } catch (Exception e) {
            throw new IllegalStateException("체크 로직 수행 중 오류 발생: " + logic.getLogicId(), e);
        }
        return CheckLogicTrace.builder()
                .matchedConditionId(compId)
                .logicId(logic.getLogicId())
                .logicDesc(logic.getLogicDesc())
                .logicClass(logic.getLogicClass())
                .logicMethod(logic.getLogicMethod())
                .callStart(callStart)
                .callEnd(LocalDateTime.now())
                .build();
    }
}
