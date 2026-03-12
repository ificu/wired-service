CREATE TABLE IF NOT EXISTS wired_services (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    svc_type VARCHAR(2) NOT NULL,
    svc_nm   VARCHAR(100) NOT NULL,
    svc_no   VARCHAR(50)  NOT NULL,
    cust_id  VARCHAR(100) NOT NULL,
    cntr_no  VARCHAR(50)  NOT NULL
);

/*   동적 체크 로직을 위한 테이블     */

CREATE TABLE IF NOT EXISTS check_logic_pool (    /* 체크 로직 Pool 관리 */
    logic_id   VARCHAR(10) NOT NULL PRIMARY KEY,  /* 체크 로직 id */
    logic_nm   VARCHAR(100) NOT NULL,   /* 체크 로직 명 */
    logic_desc   VARCHAR(50)  NOT NULL,   /* 로직 설명 */
    logic_class  VARCHAR(30) NOT NULL,   /* 로직 Class */
    logic_methd  VARCHAR(100)  NOT NULL   /* 로직 Methode */
);

CREATE TABLE IF NOT EXISTS check_condition_attr (    /* 체크 조건 항목 관리 */
    cond_attr_cd  VARCHAR(20) NOT NULL PRIMARY KEY,  /* 체크 조건 항목 코드 */
    cond_attr_nm   VARCHAR(100) NOT NULL   /* 체크 조건 항목 명 */
);

CREATE TABLE IF NOT EXISTS check_condition_attr_dtl (    /* 체크 조건 항목 상세 */
    cond_attr_cd  VARCHAR(20) NOT NULL,  /* 체크 조건 항목 코드 */
    cond_attr_val  VARCHAR(10) NOT NULL,  /* 체크 조건 항목 값 value */
    cond_attr_nm   VARCHAR(100) NOT NULL,   /* 체크 조건 항목 값 의미 */
    PRIMARY KEY (cond_attr_cd, cond_attr_val)
);

CREATE TABLE IF NOT EXISTS check_condition_composite (  /* 체크 조건 기준 조합 */
    comp_id       BIGINT PRIMARY KEY,               /* 조건 조합 아이디 */
    vald_strt_dt    DATE,  /* 유효 시작 시간 */
    vald_end_dt     DATE  /* 유효 종료 시간 */
);

CREATE TABLE IF NOT EXISTS check_condition_composite_dtl (  /* 체크 조건 기준 조합 상세 조건 */
    comp_id       BIGINT,
    cond_attr_cd  VARCHAR(20) NOT NULL,  /* 체크 조건 항목 코드 */    
    seq           BIGINT,
    cond_attr_val  VARCHAR(20) NOT NULL,  /* 체크 조건 항목 값 value */
    PRIMARY KEY (comp_id, cond_attr_cd, seq)
);


CREATE TABLE IF NOT EXISTS check_condition_logic_metrix (  /* 체크 조건 기준 처리 로직 조합 */
    comp_id       BIGINT,               /* 조건 조합 아이디 */
    order_seq     BIGINT,               /* 조회 순번 */
    logic_id   VARCHAR(20) NOT NULL,   /* 체크 로직 id */
    PRIMARY KEY (comp_id, order_seq)
);