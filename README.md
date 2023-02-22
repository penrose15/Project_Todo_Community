# project_todo_community


> Todolist-Community는 TodoList 기능과 TodoList를 팀과 공유하며 동기부여를 얻을 수 있는 서비스입니다. 평소 혼자서 TodoList를 작성하면서 혼자보단 내 TodoList와 달성도를 공유함으로써 동기부여를 얻으면 좋을 것 같다는 생각에서 시작되었습니다.

### 🛠️ 사용 기술& 라이브러리
- Java
- Spring Boot, Spring Security, JPA, Junit5, RestDocs, QueryDSL, JWT
- MySQL, Redis, AWS(EC2, RDS, S3), Jenkins, Docker
- Github, Postman

인원 : 개인(1인)

기간 : 2023.01 ~ (진행 중)

### 기능 정의 요구서
* 개인은 하루의 완성 퍼센티지를 볼 수 있음 
* 개인은 할 일 목록을 작성 가능 ✅
* 일을 끝내면 끝냈다는 표시를 할 수 있음 ✅ 
* 방들을 리스트(페이지) 형식으로 목록 구현 ✅
* 개인은 방에 참여 가능 ✅
* 개인은 방 만들 수 있음(인원 제한 가능, 강퇴 기능 활성 가능) ✅
* 방에 참여하면 각자 오늘 할 투두리스트 작성 ✅
* 방장은 규칙을 정하고 규칙을 어긴 팀원을 내쫒을 수 있음 ✅
* n일 이상 TODOLIST를 작성하지 않거나 달성도가 0%라면 자동 강퇴 ✅
* 방장은 방장자리를 다른 팀원에게 위임할 수 있다.(방장은 팀원이 됨) ✅
* 개인은 입장, 퇴장이 가능 ✅
* new  -> 카테고리를 만들어 투두리스트를 카테고리별로 분류할 수 있다
* new -> 개인의 투두리스트를 달력에 띄워서 한 눈에 볼 수 있다.

### 서버 배포 (예정)
* AWS + Docker 사용 예정
* Jenkins를 이용한 CI/CD 구축 예정
* Nginx 를 통한 로드 밸런싱 구축 예정
