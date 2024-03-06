## 프로젝트 이름
쇼핑몰 웹 페이지

## 사용 방법
[!postman json파일](/Project_mall.postman_collection.json)
1. 회원가입
   - http://localhost:8080/users/register
   - 데이터 설정을 아래와 같이 해준다.
   - 비활성 사용자(INACTIVE)로 가입된다.
2. 로그인
   - http://localhost:8080/login
   - 아이디와 비밀번호가 전달되면, JWT가 발급된다.
3. 회원정보 수정
   - http://localhost:8080/users/update
   - 이름, 닉네임, 이메일, 전화번호, 연령대를 입력하면 일반 사용자(GENERAL)로 전환된다.
4. 사업자 사용자로 전환 신청
   - http://localhost:8080/users/general/updateBusinessNumber
   - 일반 사용자의 경우 사업자 등록 번호를 입력하면, 사업자 사용자 전환 신청이 된다.
5. 사업자 사용자 전환 신청(조회/수락/거절)
   - 프로젝트가 실행될 때, 자동으로 생성되는 admin 계정으로 로그인하여 jwt를 발급받는다.
   - admin계정(username: admin, password: admin)
   - http://localhost:8080/admin/business-requests
     - 사업자 사용자 전환 신청 목록을 조회할 수 있다.
   - http://localhost:8080/admin/business-requests/{requestId}/approve
     - 승인할 데이터의 id(requestId)를 넣고, 승인할 수 있다.
   - http://localhost:8080/admin/business-requests/{requestId}/reject
     - 거절할 데이터의 id(requestId)를 넣고, 거절할 수 있다.
6. 쇼핑몰 (정보수정 및 오픈요청)
   - 사업자 사용자는 자동으로 생성된 쇼핑몰의 정보를 수정할 수 있다.
     - http://localhost:8080/shops/{shopId}/update
     - 쇼핑몰의 정보를 수정했다면, 쇼핑몰 오픈 요청을 보낼 수 있다.
   - http://localhost:8080/shops/{shopId}/open-request
     - 관리자는 쇼핑몰 오픈 요청 목록을 조회할 수 있다.
   - http://localhost:8080/admin/shops/requests
     - 관리자는 쇼핑몰 오픈 요청을 승인할 수 있다.
   - http://localhost:8080/admin/open-requests/{shopId}/approve
     - 승인하면 쇼핑몰의 상태가 OPEN으로 변경된다.
   - 관리자는 쇼핑몰 오픈 요청을 거절할 수 있다.
     - http://localhost:8080/admin/open-requests/{shopId}/reject
     - 이때 이유를 작성해야 한다.

7. 쇼핑몰 폐쇄 요청
   - OPEN 상태의 쇼핑몰은 쇼핑몰 주인이 폐쇄 신청을 할 수 있다.
   - http://localhost:8080/shops/{shopId}/close-request
   - 관리자는 폐쇄 신청을 수락할 수 있다.
   - http://localhost:8080/admin/close-requests/{shopId}/approve

8. 쇼핑몰 조회
- OPEN 상태의 쇼핑몰들을 조회할 수 있다.
- http://localhost:8080/shops/list

