# project_todo_community


> Todolist-Community는 TodoList 기능과 TodoList를 팀과 공유하며 동기부여를 얻을 수 있는 서비스입니다. 평소 혼자서 TodoList를 작성하면서 혼자보단 내 TodoList와 달성도를 공유함으로써 동기부여를 얻으면 좋을 것 같다는 생각에서 시작되었습니다.

인원 : 개인(1인)

기간 : 2023.01 ~ 현재

## 개발환경
* SpringBoot(2.7.7)
* Java 11
* Gradle

## 테스트 환경
* Junit5

## 🛠️ 사용 기술& 라이브러리
- Java
- Spring Boot, Spring Security, JPA, Junit5, RestDocs, QueryDSL, JWT
- MySQL, Redis, AWS(EC2, RDS, S3), Jenkins, Docker
- Github, Postman



## 기술 블로그(Trouble Shooting)
➡️ [기술 블로그](https://velog.io/@penrose_15/series/%EA%B0%9C%EC%9D%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8)

## 📘요구사항 정의서 
<img src="https://user-images.githubusercontent.com/96187152/220655507-d25cc744-ac58-40f5-922d-708413ea8278.png" width="60%"/>

[요구사항 정의서](https://seongjuhong.notion.site/a9a979d05d934368a9ff2962c0d3e9e7?v=44533757dd8943d88998f7bc90debdc4)

## 화면 설계서
<img src="https://user-images.githubusercontent.com/96187152/220656620-947aae96-4e29-4f6c-9e50-e41c10a5cbd7.png" width="60%"/>

[화면 설계서](https://www.figma.com/file/fWTYFoXGeOJzuV0SYEIHRh/Todolist-community-%ED%99%94%EB%A9%B4-%EC%84%A4%EA%B3%84%EC%84%9C?node-id=0%3A1&t=HcQGkZ4QCedzQdhm-1)

## ERD 설계
<img src="https://user-images.githubusercontent.com/96187152/220660945-b05045a1-9c0e-4fcd-a2a4-321611e7e76f.png" width="60%"/>

## 유스케이스 다이어그램
<img src="https://user-images.githubusercontent.com/96187152/220667102-365db9e1-a689-45ca-a4cc-02ff018ce3da.png" width="60%"/>

## 💻구현한 기능

### 1. RestfulAPI 서버 개발

- TodoList 작성/수정/조회/삭제 API 개발
- Team 추가/조회/가입/탈퇴 API 개발
- User 회원가입/팀 가입/탈퇴/방장 위임 기능 구현 및 API 제작
- 검색기능 구현
    - QueryDSL을 이용하여 구현

### 2. @Scheduled를 활용한 일괄 삭제 기능 구현

- n일 연속으로 참여율이 저조한 팀원 대상으로 특정 시간(23:59)에 일괄 강퇴 시키는 기능 구현
- ThreadPoolTaskScheduler와 Async 활용하여 비동기 스레드 풀 추가

### 3. JWT 토큰을 활용하여 인증/인가 구현

- accessToken, refreshToken 구현
- refreshToken은 Redis에 저장 후 accessToken 만료할 때 마다 refreshToken 확인 후 accessToken 재발급 (redis는 Docker위에 실행)

### 4. Junit5를 활용한 단위 테스트 작성 및 RestDocs를 활용한 API 문서 작성

- Repository, Service, Controller 단위 테스트구현
- RestDocs를 활용한 API 문서 작성
- LocalDate를 단위 테스트에서 적용할 수 없는 문제를이는 Clock을 Bean으로 등록한 후 Mock데이터로 만들어 해결.

### 5. 온 프레미스환경 인프라 구축 및 CI/CD 구축

- AWS EC2(Linux2)에서 서버를 구축하고 RDS와 연결하여 데이터를 저장 및 관리
- Docker에 Jenkins를 띄워 github push/merge시 Jenkins로 CI/CD 구축
    - Jenkins를 Docker로 띄울 때, 내부에 Docker를 사용하지 않는 문제를 필요한 디렉토리를 mount하여 문제 해결
