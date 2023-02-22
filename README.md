# project_todo_community


> Todolist-Community는 TodoList 기능과 TodoList를 팀과 공유하며 동기부여를 얻을 수 있는 서비스입니다. 평소 혼자서 TodoList를 작성하면서 혼자보단 내 TodoList와 달성도를 공유함으로써 동기부여를 얻으면 좋을 것 같다는 생각에서 시작되었습니다.

인원 : 개인(1인)

기간 : 2023.01 ~ 현재

### 개발환경
* SpringBoot(2.7.7)
* Java 11
* Gradle

### 테스트 환경
* Junit5

### 🛠️ 사용 기술& 라이브러리
- Java
- Spring Boot, Spring Security, JPA, Junit5, RestDocs, QueryDSL, JWT
- MySQL, Redis, AWS(EC2, RDS, S3), Jenkins, Docker
- Github, Postman



### 기술 블로그(Trouble Shooting)
➡️ [기술 블로그](https://velog.io/@penrose_15/series/%EA%B0%9C%EC%9D%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8)

### 📘요구사항 정의서 
<img src="https://user-images.githubusercontent.com/96187152/220655507-d25cc744-ac58-40f5-922d-708413ea8278.png" width="60%"/>

[요구사항 정의서](https://seongjuhong.notion.site/a9a979d05d934368a9ff2962c0d3e9e7?v=44533757dd8943d88998f7bc90debdc4)

### 화면 설계서
<img src="https://user-images.githubusercontent.com/96187152/220656620-947aae96-4e29-4f6c-9e50-e41c10a5cbd7.png" width="60%"/>

[화면 설계서](https://www.figma.com/file/fWTYFoXGeOJzuV0SYEIHRh/Todolist-community-%ED%99%94%EB%A9%B4-%EC%84%A4%EA%B3%84%EC%84%9C?node-id=0%3A1&t=HcQGkZ4QCedzQdhm-1)

### ERD 설계
<img src="https://user-images.githubusercontent.com/96187152/220660945-b05045a1-9c0e-4fcd-a2a4-321611e7e76f.png" width="60%"/>

### 유스케이스 다이어그램
<img src="https://user-images.githubusercontent.com/96187152/220667102-365db9e1-a689-45ca-a4cc-02ff018ce3da.png" width="60%"/>

### 기능 구현 
TodoList 업로드/수정/삭제/조회 기능 구현 및 REST API 제작

카테고리 CRUD 기능 및 REST  API 제작

팀 기능 구현 및 REST  API 제작

- 팀 가입/탈퇴 기능 구현
- 방장 위임 기능 구현
- n일 이상으로 TODOLIST를 작성하지 않거나 달성도가 0%라면 자동 강퇴 기능 구현(단, 방장이 설정 가능)
    - @Scheduled 를 활용하여 특정 시간에 일괄 처리

복잡한 쿼리문이 요구되는 경우 QueryDSL로 구현

Spring Security 를 활용한 Security Filter Chain 구현

로그인 기능
- **JWT** 토큰으로 인증/인가 구현
- AccessToken과 RefreshToken 구현
- RefreshToken은 **Redis**에 저장 (Redis는 Docker 위에 실행)

비밀번호 분실시 임시 비밀 번호 이메일로 전송

**Junit5**로 단위 테스트 작성(repository, service, controller)

Unit Test 작성과 RestDocs를 활용한 API문서 작성

**AWS**(EC2, S3, RDS)에 서버 배포

**Docker, Jenkins**로 CI/CD 구현

도메인 적용
