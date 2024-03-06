## 사용자 인증 및 권한 관리
1. `admin`의 자동 생성(회원 가입 X)
- MissionShopApplication.java의 main을 실행하면 `admin` 관리자는 자동으로 생성된다. 
![admin 생성](/md/ExecuteReadmeIMG/1.admin생성.PNG)
- `http://localhost:8080/token/issue`에서 `admin` jwt 발급
![admin jwt](/md/ExecuteReadmeIMG/2.admin-JWT.PNG)

## 아래의 모든 과정은 authorization 탭에서 bearer token 입력 필수
2. 사용자 
- `http://localhost:8080/users/register` 에서 사용자 회원가입이 가능하다.
![test2 회원가입](/md/ExecuteReadmeIMG/3.사용자회원가입test2.PNG)
- 첫 회원 가입시 이름과 비밀번호로만 가입하기 때문에 비활성 사용자(ROLE_INACTIVE)로 설정된다.
![비인가 사용자](/md/ExecuteReadmeIMG/4.비인가사용자.PNG)
![test2의 JWT](/md/ExecuteReadmeIMG/5.test2의JWT.PNG)
- `http://localhost:8080/users/update` 에서 서비스를 이용하기 위해 추가 정보를 입력한다.
![사용자 update](/md/ExecuteReadmeIMG/6.사용자update.PNG)
![토큰 입력](/md/ExecuteReadmeIMG/7.사용자update-토큰입력.PNG)
- 추가 정보를 입력하면 일반 사용자로 설정된다.
![일반 사용자](/md/ExecuteReadmeIMG/8.사용자update확인.PNG)
- `http://localhost:8080/users/business` 에서 일반 사용자가 username, password, 사업자 등록 번호, token을 입력하여 사업자 전환 신청을 할 수 있다.
![사업자 전환 신청](/md/ExecuteReadmeIMG/9.사업자전환신청.PNG)
![토큰입력](/md/ExecuteReadmeIMG/10.사업자전환신청토큰입력.PNG)
- apply가 된 것 확인
![apply](/md/ExecuteReadmeIMG/11.apply.PNG)
- `http://localhost:8080/users/admin/applyList` 에서 관리자는 사업자 전환 신청 목록을 확인할 수 있다.
![apply 확인](/md/ExecuteReadmeIMG/12.관리자apply확인.PNG)
- `http://localhost:8080/users/admin/apply/accept-refuse` 에서 관리자는 사업자 전환 신청을 수락/거절할 수 있다.
![apply 수락](/md/ExecuteReadmeIMG/13.사업자전환수락.PNG)
- 관리자가 사업자 전환을 수락하면 데이터베이스에서도 사용자의 역할이 바뀐 것을 확인할 수 있다.
![role business](/md/ExecuteReadmeIMG/14.수락후role변화.PNG)

## 중고거래 중개하기
![사용자 목록](/md/ExecuteReadmeIMG/15.사용자목록.PNG)
- `http://localhost:8080/items/register` 에서 일반 사용자는 중고 거래 물품을 등록할 수 있습니다. 
![중고거래 등록 성공](/md/ExecuteReadmeIMG/16.중고거래등록성공.PNG) 
![데이터베이스에서도 확인 가능](/md/ExecuteReadmeIMG/19.데이터베이스중고거래물품등록확인.PNG)
- 사업자인 test2는 물품을 등록할 수 없습니다.
![중고거래 등록 실패](/md/ExecuteReadmeIMG/17.사업자중고거래등록실패.PNG)
- `http://localhost:8080/items/itemAllList` 에서 등록된 중고거래 물품 목록을 확인할 수 있습니다. 
![중고거래 물품 확인](/md/ExecuteReadmeIMG/18.중고거래물품확인.PNG)
- `http://localhost:8080/items/update` 에서 등록한 중고거래 물품을 수정할 수 있습니다. 
![물품수정](/md/ExecuteReadmeIMG/20.중고거래상품수정.PNG)
![물품수정 데이터베이스](/md/ExecuteReadmeIMG/21.중고거래물품수정데이터베이스확인.PNG)
- `http://localhost:8080/items/delete` 에서 등록한 중고거래 물품을 삭제할 수 있습니다.
![중고거래 물건 삭제](/md/ExecuteReadmeIMG/22.중고거래상품삭제.PNG)
![물건 삭제 데이터베이스](/md/ExecuteReadmeIMG/23.물건삭제데이터베이스.PNG)
- `http://localhost:8080/items/buyRequest` 에서 중고거래 구매 제안을 할 수 있다.
  - 구매하고자 하는 물건의 이름(title)로 요청, 금액 제시 
![구매 제안](/md/ExecuteReadmeIMG/24.test2구매제안.PNG)
- `http://localhost:8080/items/offer/read` 에서 구매 제안을 한 사용자와 물품 등록자는 구매 제안을 확인할 수 있다.
![구매 제안 읽기](/md/ExecuteReadmeIMG/25.구매제안읽기.PNG)
  - 구매 제안과 관련 없는 사용자는 구매 제안을 확인할 수 없다.
![구매 제안 읽기 실패](/md/ExecuteReadmeIMG/26.구매제안자가아니면읽기실패.PNG)
- `http://localhost:8080/items/offer/accept-refuse` 에서 중고거래 물품 등록자는 구매 제안을 수락/거절 할 수 있다.
![구매 제안 수락](/md/ExecuteReadmeIMG/27.구매제안수락.PNG)
![제안 수락 데베](/md/ExecuteReadmeIMG/28.구매제안수락데이터베이스.PNG)
- `http://localhost:8080/items/offer/confirm` 에서 구매 확정을 할 수 있다.
  - 구매 확정시 같은 물품의 다른 제안은 거절로 변경된다.


## 쇼핑몰 운영하기
- 일반사용자가 사업자 사용자로 전환될 때 자동으로 준비중 상태의 쇼핑몰이 추가 된다.
![준비중 쇼핑몰](/md/ExecuteReadmeIMG/29.쇼핑몰준비중.PNG)
- `http://localhost:8080/shops/update` 에서 쇼핑몰 주인이 자유롭게 수정이 가능하다
![쇼핑몰 수정](/md/ExecuteReadmeIMG/30.쇼핑몰수정.PNG)
![쇼핑몰 수정 데베](/md/ExecuteReadmeIMG/31.쇼핑몰수정-데베.PNG)
- 쇼핑몰 카데고리는 서비스 제작자에 의해 미리 정해진다.(최소 5개)
![카테고리](/md/ExecuteReadmeIMG/32.쇼핑몰카테고리.PNG)
![지원하지 않는 카테고리](/md/ExecuteReadmeIMG/33.지원하지않는카테고리.PNG)
- `http://localhost:8080/shops/apply/open` 에서 이름, 소개, 분류가 전부 작성된 상태일 때 쇼핑몰개설 신청을 할 수 있다.
  - 작성되지 않은 항목이 있을 경우 신청할 수 없다.
  - 실패
![쇼핑몰 오픈 신청](/md/ExecuteReadmeIMG/34.쇼핑몰오픈신청실패.PNG)
  - 성공
![쇼핑몰 지원2](/md/ExecuteReadmeIMG/35.쇼핑몰지원2.PNG)
![지원성공](/md/ExecuteReadmeIMG/36.쇼핑몰오픈지원성공.PNG)
![status](/md/ExecuteReadmeIMG/38.개설신청status.PNG)
- 관리자는 `http://localhost:8080/shops/apply/read` 에서 쇼핑몰 개설 신청 목록을 볼 수 있다.
![신청 목록](/md/ExecuteReadmeIMG/37.개설신청목록.PNG)
- 관리자는 `http://localhost:8080/shops/apply/open/acceptRefuse` 에서 개실 신청을 허가/불허할 수 있다.
  - 불허
![불허](/md/ExecuteReadmeIMG/39.불허.PNG)
![불허 데베](/md/ExecuteReadmeIMG/40.불허-데베.PNG)
  - 허락
![허가](/md/ExecuteReadmeIMG/41.허가.PNG)
![허가-데베](/md/ExecuteReadmeIMG/42.허가-데베.PNG)
---
- 쇼핑몰 관리
- 쇼핑몰 주인은 `http://localhost:8080/shop/items/register` 에서 상품을 등록할 수 있다.
![상품등록완료](/md/ExecuteReadmeIMG/43.상품등록완료.PNG)
![상품등록완료-데베](/md/ExecuteReadmeIMG/44.상품등록완료-데베.PNG)
- 쇼핑몰 주인은 `http://localhost:8080/shop/items/update` 에서 등록한 상품을 수정할 수 있다.
![상품수정](/md/ExecuteReadmeIMG/45.상품수정.PNG)
![상품수정-데베](/md/ExecuteReadmeIMG/46.상품수정-데베.PNG)


- 쇼핑몰 조회
- 비활성 사용자를 제외한 사용자는 `http://localhost:8080/shops/search` 에서 쇼핑몰을 조회할 수 있다.
- 이름, 쇼핑몰 분류를 조건으로 쇼핑몰을 검색할 수 있다. 
- (검색할 수 있는 방법 : 1. 이름 2. 쇼핑몰 분류 3. 이름, 쇼핑몰 분류 4. 조건 없이)
![검색](/md/ExecuteReadmeIMG/48.쇼핑몰검색.PNG)


- 쇼핑몰 상품 검색
- `http://localhost:8080/shop/items/search` 에서 비활성 사용자를 제외한 사용자는 상품을 검색할 수 있다.
- 조회되는 상품이 등록된 쇼핑몰에 대한 정보도 함께 제공된다.
![상품 검색](/md/ExecuteReadmeIMG/47.상품검색.PNG) 
- 조건없이 상품 검색
![조건상품](/md/ExecuteReadmeIMG/49.조건없이상품검색.PNG)


- 상품 구매
- 비활성 사용자를 제외한 사용자는 `http://localhost:8080/shop/items/buyReques` 에서 상품을 구매요청할 수 있다.
![구매요청](/md/ExecuteReadmeIMG/50.상품구매요청.PNG)
![구매요청-데베](/md/ExecuteReadmeIMG/51.구매요청-데베.PNG)
- 구매 요청 후 사용자는 `http://localhost:8080/shop/items/buyRequest/sendMoney` 에서 구매에 필요한 금액을 전달한다.
![금액전송](/md/ExecuteReadmeIMG/53.금액전송.PNG)
![금액전송-데베](/md/ExecuteReadmeIMG/54.금액전송-데베.PNG)
- 쇼핑몰 주인은 `http://localhost:8080/shop/items/buyRequest/check` 에서 구매 요청을 확인할 수있다.
- 금액이 입금되지 않았을 때
![금액입금x](/md/ExecuteReadmeIMG/52.구매요청확인-금액입금x.PNG)
- 금액이 입금되었으면 요청을 수락하고, 재고가 자동으로 갱신된다.
![요청수락](/md/ExecuteReadmeIMG/55.요청수락.PNG)
![요청수락-데베](/md/ExecuteReadmeIMG/56.요청수락-데베.PNG)
![재고갱신](/md/ExecuteReadmeIMG/57.재고갱신.PNG)
- 구매 요청이 수락되면 구매 요청을 취소할 수 없다
![취소 실패](/md/ExecuteReadmeIMG/58.재고갱신후취소불가.PNG)


