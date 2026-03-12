# DB접속 확인
http://localhost:8080/h2-console
jdbc:h2:file:./src/main/resources/db/testdb

# 테이블 구조
```mermaid
erDiagram
    check_logic_pool["check_logic_pool (체크 로직 Pool)"] {
        VARCHAR logic_id PK "로직 ID"
        VARCHAR logic_nm "로직 명"
        VARCHAR logic_desc "로직 설명"
        VARCHAR logic_class "로직 Class명"
        VARCHAR logic_methd "로직 Method명"
    }

    check_condition_attr["check_condition_attr (체크 조건 항목)"] {
        VARCHAR cond_attr_cd PK "조건 항목 코드"
        VARCHAR cond_attr_nm "조건 항목 명"
    }

    check_condition_attr_dtl["check_condition_attr_dtl (체크 조건 항목 상세)"] {
        VARCHAR cond_attr_cd PK,FK "조건 항목 코드"
        VARCHAR cond_attr_val PK "조건 항목 값"
        VARCHAR cond_attr_nm "조건 항목 값 의미"
    }

    check_condition_composite["check_condition_composite (체크 조건 조합)"] {
        BIGINT comp_id PK "조건 조합 ID"
        DATE vald_strt_dt "유효 시작 일자"
        DATE vald_end_dt "유효 종료 일자"
    }

    check_condition_composite_dtl["check_condition_composite_dtl (체크 조건 조합 상세)"] {
        BIGINT comp_id PK,FK "조건 조합 ID"
        VARCHAR cond_attr_cd PK,FK "조건 항목 코드"
        BIGINT seq PK "순번"
        VARCHAR cond_attr_val "조건 항목 값"
    }

    check_condition_logic_metrix["check_condition_logic_metrix (조건별 처리 로직 매트릭스)"] {
        BIGINT comp_id PK,FK "조건 조합 ID"
        BIGINT order_seq PK "실행 순번"
        VARCHAR logic_id FK "로직 ID"
    }

    check_condition_attr ||--o{ check_condition_attr_dtl : "항목의 허용값 목록"
    check_condition_composite ||--o{ check_condition_composite_dtl : "조합의 세부 조건 (AND)"
    check_condition_attr ||--o{ check_condition_composite_dtl : "세부 조건의 항목 참조"
    check_condition_composite ||--o{ check_condition_logic_metrix : "조합에 연결된 실행 로직"
    check_logic_pool ||--o{ check_condition_logic_metrix : "실행할 로직 참조"


```

## 동적 조건 체크 쿼리
```sql
SELECT
    comp_id,
    cond_attr_cd,
    LISTAGG(cond_attr_val, ',') WITHIN GROUP (ORDER BY seq) AS cond_attr_val
FROM CHECK_CONDITION_COMPOSITE_DTL
GROUP BY comp_id, cond_attr_cd;

```

## 쿼리 결과

| COMP_ID | COND_ATTR_CD | COND_ATTR_VAL|
| --- | --- | --- |
| 1 | svc_cd | C |
| 2 | chg_cd | A1 |
| 2 | svc_cd | I |
| 3 | chg_cd | C8,C6 |
| 3 | svc_cd | C |
| 3 | svc_use_ctg | 01 |


## 체크로직 수행 정보 확인 쿼리
```sql
SELECT
    clp.logic_id,
    clp.logic_desc,
    clp.logic_class,
    clp.logic_methd
FROM check_condition_logic_metrix cclm
INNER JOIN check_logic_pool clp ON cclm.logic_id = clp.logic_id
WHERE cclm.comp_id = 3
ORDER BY cclm.order_seq
```

## 쿼리 결과

| LOGIC_ID  |	LOGIC_DESC  |	LOGIC_CLASS  |	LOGIC_METHD  |
| --- | --- | --- | --- |
| 2000000831  |	SKB미납금존재  |	BillingHistoryConstraint  |	checkUnpaidBalance  |
| 2000001922  |	3개월내해지가입제한  |	SubscrpTermConstraint  |	checkReSubscriptionWithinThreeMonths  |
| 2000002723  |	신용가입제한체크  |	LineLimitConstraint  |	checkCreditSubscriptionRestriction  |
| 2000003023  |	동일장소인터넷tv 제한  |	SubscrpAgencyConstraint  |	checkSameLocationInternetTvRestriction  |




## 동적 호출 구조
```mermaid
sequenceDiagram
    actor Client
    participant Controller as WiredServiceController
    participant Service as WiredServiceServiceImpl
    participant CondMatcher as ConditionMatcher
    participant CondMapper as CheckConditionMapper
    participant AppCtx as ApplicationContext
    participant CheckLogic as CheckLogic (e.g. LineLimitConstraint)
    participant WiredMapper as WiredServiceMapper

    Client->>Controller: POST /api/wired-services (WiredServiceReq)
    Controller->>Service: create(WiredServiceIn)

    Note over Service: 1. DB에서 전체 조건 조합 조회
    Service->>CondMapper: findAllConditions()
    CondMapper-->>Service: List[CheckConditionComposite]

    Note over Service: 2. 입력값과 매칭되는 comp_id 목록 추출
    Service->>CondMatcher: match(conditions, in)
    CondMatcher-->>Service: List[matchedCompIds]

    Note over Service: 3. 매칭된 comp_id 별 체크 로직 수행 및 trace 수집
    loop 매칭된 각 compId
        Service->>CondMapper: findLogicsByCompId(compId)
        CondMapper-->>Service: List[CheckLogic]

        loop 각 CheckLogic
            Note over Service: invokeCheckLogic(compId, logic, serviceNumber)
            Service->>AppCtx: getBean(logic_class)
            AppCtx-->>Service: bean instance
            Service->>CheckLogic: logic_methd(serviceNumber)
            CheckLogic-->>Service: void
            Note over Service: callStart / callEnd 기록 → CheckLogicTrace 생성
        end
    end

    Note over Service: 4. 이상이 없으면 아래 실행 실제 업무처리 로직 수행
    Service->>WiredMapper: insert(WiredServiceQuery)
    WiredMapper-->>Service: (generated id)

    Service-->>Controller: WiredServiceOut (with ExecutionTrace)
    Controller-->>Client: 201 Created (WiredServiceRes)
```