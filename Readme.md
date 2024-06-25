# 특강 신청 서비스

## 1. Requirements

### &emsp; 1-1. 아래 2가지 API 를 구현합니다.
&emsp;&emsp; - 특강 신청 API <br />
&emsp;&emsp; - 특강 신청 여부 조회 API <br /><br />
&emsp;&emsp; - 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다. <br />
&emsp;&emsp; - 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다. <br />
&emsp;&emsp; - 동시성 이슈를 고려하여 구현합니다. <br />

### &emsp; 1-2. API Specs
### &emsp;&emsp; 1-2-1. (핵심) 특강 신청 API `POST /lectures/apply` <br />
&emsp;&emsp;&emsp;&emsp; - 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다. <br />
&emsp;&emsp;&emsp;&emsp; - 동일한 신청자는 한 번의 수강 신청만 성공할 수 있습니다. <br />
&emsp;&emsp;&emsp;&emsp; - 각 강의는 선착순 30명만 신청 가능합니다. <br />
&emsp;&emsp;&emsp;&emsp; - 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다. <br />
&emsp;&emsp;&emsp;&emsp; - 어떤 유저가 특강을 신청했는지 히스토리를 저장해야한다. <br /><br />

### &emsp;&emsp; 1-2-2. (기본) 특강 목록 API `GET /lectures`  <br />

&emsp;&emsp;&emsp;&emsp; - 단 한번의 특강을 위한 것이 아닌 날짜별로 특강이 존재할 수 있는 범용적인 서비스로 변화시켜 봅니다. <br />
&emsp;&emsp;&emsp;&emsp; - 이를 수용하기 위해, 특강 엔티티의 경우 기본 과제 SPEC 을 만족하는 설계에서 변경되어야 할 수 있습니다. <br />
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; - 수강신청 API 요청 및 응답 또한 이를 잘 수용할 수 있는 구조로 변경되어야 할 것입니다. <br />
&emsp;&emsp;&emsp;&emsp; - 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기전 목록을 조회해볼 수 있어야 합니다. <br />
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; - 추가로 정원이 특강마다 다르다면 어떻게 처리할것인가..? <br />

### &emsp;&emsp; 1-2-3. (기본) 특강 신청 완료 여부 조회 API `GET /lectures/application/{userId}`  <br />

&emsp;&emsp;&emsp;&emsp; - 특정 userId 로 특강 신청 완료 여부를 조회하는 API 를 작성합니다. <br />
&emsp;&emsp;&emsp;&emsp; - 특강 신청에 성공한 사용자는 성공했음을, 특강 등록자 명단에 없는 사용자는 실패했음을 반환합니다. (true, false) <br />

## 2. TC Case


### &emsp; 1-1.  Case
&emsp;&emsp; 1) 유효하지 않은 사용자로 강의 신청 시도 <br />
&emsp;&emsp; 2) 강의 신청 테스트(단일유저) <br />
&emsp;&emsp; 3) 강의 신청 여부 조회 테스트(수강중인 강의가 있을 떄) <br />
&emsp;&emsp; 4) 강의 신청 여부 조회 테스트(수강중인 강의가 없는 상태) <br />
&emsp;&emsp; 5) 모든 강의 조회 테스트 <br />
&emsp;&emsp; 6) 중복 신청 테스트 <br />
&emsp;&emsp; 7) 강의 오픈 전 신청 테스트 <br />
&emsp;&emsp; 8) 강의 신청 테스트(다수유저) <br />


### 3. ERD

![STEP3_ERD](https://github.com/qdw0719/second-week/assets/84309890/7f827f5b-7800-44bb-91f3-78d23ee55dd1)




### 4. etc.. 고민,,, 고난... 역경....

1. STEP 3 를 하면서,, H2로 할 때는 왜 동시성 제어가 되질 않았는가,,, <br /> → h2 db를 사용하다가 결국 MySQL로 변경...  
2. 




