# 쇼핑몰 구축
- 일반 사용자는 중고거래가 가능하며, 사업자는 인터넷 쇼핑몰을 운영할 수 있게 해주는 쇼핑몰 사이트를 만들어보자.
- 요구사항의 내용은 프론트엔드 없이, 백엔드만 개발한다. 
- 별도의 프론트엔드 클라이언트가 존재한다고 생각하고 서버를 만들고, Postman으로 테스트를 한다. 
- 단, CORS는 지금은 고려하지 않는다.
---
## 스택
- Spring Boot 3.2.3
- Spring Boot Data JPA
- SQLite
- JWT
- Spring Security

## 실행
1. 본 Repository를 clone 받는다.
2. Intellij IDEA를 이용해 clone 받은 폴더를 연다.
3. [MissionShopApplication.java](/src/main/java/com/example/Mission_shop/MissionShopApplication.java) 의 `main`을 실행한다.
    - 테스트 데이터를 사용하지 않기 때문에 한번 프로젝트를 실행하고 종료 (`ddl-auto` 옵션으로 테이블 자동 생성)
    - `spring.data.jpa.hibernate.ddl-auto`를 `update`로 수정
    - 이후 정상적으로 프로젝트 실행
4. Postman을 이용한 실행 방법 => **[바로가기](/md/ExecuteReadme.md)**
- 필요시 [포스트맨 참고](/md/Mission_Shop.postman_collection.json) collection을 import 하여 사용하세요
---
## 필수 기능 요구사항
- [1. 사용자 인증 및 권한 처리 ](/md/1.User-authentication&authorization-handling.md)
- [2. 중고거래 중개하기 ](/md/2.Used-trade.md)
- [3. 쇼핑몰 운영하기 ](/md/3.ShoppingMall.md)

---
## 구현 방식 설명
-
## 회고
```text
전체적인 요구사항을 읽고나서 무엇을 먼저 시작해야 할지 갈피를 잡지 못해 시간을 많이 소요했다.
만들면서 security 사용부분에서 이해가 부족해 어려워 한다는 걸 느꼈고 image 추가나 추가과제 등 구현하지 못한 부분이 있어서 아쉽다.
초반에는 빠르게 구현하는 듯 했으나 후반으로 갈 수록 오류가 많아지고 시간 분배에 실패해 좀 더 완성도 있는 구현을 하지 못한 것이 아쉬웠다.
```