- # 익명 의견 교환 커뮤니티 <😺Cat-In-Box>

  고양이를 사랑하는 사람들이 모여 관련 이야기를 익명으로 나누는 커뮤니티 공간

  




## Stack

- Language : Java
- Framwork : SpringBoot
- DB : MariaDB
- etc... bootstrap, Thymleaf, JPA







## 어플리케이션 로직

![image-20240111155225357](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155225357-1704956881526-1.png)

![image-20240111155236532](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155236532-1704956881527-3.png)







## 데이터베이스 로직

![image-20240111155323112](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155323112-1704956881527-5.png)

![image-20240111155336515](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155336515-1704956881527-7.png)







## 주요 구현 기능

| 번호 | 구현 기능   |
| ---- | ----------- |
| 1    | 게시판 기능 |
| 2    | 게시글 기능 |
| 3    | 댓글 기능   |
| 4    | 검색 기능   |



### 1️⃣ 게시판 기능

- Categoryid와 Board Entity를 1:n 관계로 매핑

- Category Entity의 테이블 수에 따라 View에 출력하여 url에 따라 Board List가 출력됨

- 해당 로직은 BoardRepository 및 BoardCategoryRepository 인터페이스와 BoardService 및 BoardCategoryService 클래스에서 구현

- 전체 게시글 보기, 게시판별 게시글 보기 가능

  ![image-20240111155750824](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155750824-1704956881527-9.png)

  ![image-20240111155808022](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111155808022-1704956881527-11.png)



### 2️⃣ 게시글 기능

- 제목과 내용으로 구성된 게시글 CRUD 기능 구현

- 게시글 작성 시 게시판, 제목설정, 내용설정, 패스워드 설정이 가능

- 게시글 작성 시 파일 첨부 가능

- 수정 및 삭제가 가능하며 button 클릭 시 패스워드를 입력하고, 작성시 입력했던 password 속성과 일치하면 기능 수행

  ![image-20240111164116522](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164116522.png)

  ![image-20240111164134796](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164134796.png)

  ![image-20240111164144947](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164144947.png)

  ![image-20240111164207526](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164207526.png)

  ![image-20240111164231021](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164231021.png)





### 3️⃣댓글 기능

- Comment Entity와 Board Entity가 n:1의 관계로 하나의 Board에 다수의 comment mapping 기능 구현

- 작성자, 패스워드, 내용을 작성하여 댓글 작성 수행 시 DB에 데이터가 생성됨

- 댓글 작성 후 작성된 댓글을 해당 Board에서 List형태로 출력 

- 작성된 댓글은 삭제 UI 클릭 시 Password를 입력해야하며 삭제 시 입력한 패스워드와 작성시 입력한 패스워드가 동일하다면 삭제 수행

  ![image-20240111164732621](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164732621.png)

  ![image-20240111164744679](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164744679.png)

  ![image-20240111164756169](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164756169.png)

  ![image-20240111164806469](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164806469.png)

  ![image-20240111164817518](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111164817518.png)





### 4️⃣ 검색 기능

- 전체 게시글 조회 / 카테고리별 게시글 조회 시 하단에 검색창 생성됨

- 전체 게시글 내 검색, 카테고리별 게시글 내 검색 가

- 해당 메서드는 Repository와 Service 패키지에서 구현되었음

- searchType으로 제목(title)과 내용(content)을 드롭다운으로 선택할 수 있으며 선택한 필드에 따라  board Entity의 title, content를 탐색

  ![image-20240111165249064](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111165249064.png)

  ![image-20240111165314915](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111165314915.png)

  ![image-20240111165335492](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111165335492.png)

  

---

# 못다한 구현

| 번호 | 구현 기능        |
| ---- | ---------------- |
| 1    | 게시글 추가 기능 |
| 2    | 해쉬태그 기능    |

### 1️⃣ 게시글 추가 기능

- 1-1) 수정 시 이미지 삽입 및 이미지 삭제

  - 요구사항 정의서를 정독하지 않아 게시글을 작성할 때 이미지를 삽입하도록 구현함 

  - Service와 View에서 얽히고 설켜 일단 보류함

  - file 삭제 시에도 password를 입력받는데, password 로직을 한데 묶을 수 있을까? 계속 반복되는 CRUD 로직들도? 궁금하다

    

- 1-2) 단일 게시글 조회시 카테고리 내 이전글, 다음글 이동 기능 

  - Table을 잘 분리해둬서 Url 쿼리만 잘 짜면 쉽게 구현 가능할 것 같은데, 처음부터 url 매핑을 소홀히 생각함
  - 엔드포인트 제안이 있었으나 뒤늦게 확인하고 수정하였고 끝부분에 점점 복잡해져가는 View 코드를 잡고 세시간 가까이 씨름하다가 결국 제출 시점에 처음 버전을 제출함
  - 하지만 덕분에 @RequestParam, @PathVariable을 사용하였을 때 장점을 알게되어 큰 소득이였음

  

### 2️⃣ 해쉬태그 기능

- DB에 해쉬태그 Table을 생성해서 매핑 하려고 하였으나, 시간 내 구현하지 못할 것 같아 보류함
- Query문이 DB 진입장벽이었는데 MySQL workbench에서 하니 직접 쿼리를 짤 일이 거의 없었음. DB에 데이터가 처음 쌓이는 걸 봤을 때 뛸듯이 좋았음
- Board Category - Board, Board - Comment 에 One To Many 상관관계를 설정하는데 처음엔 정말 어려웠고, 두번째엔 감이 잡힐랑 말랑함. 해쉬태그 기능으로 한번 더 도전해보고픔





---

## 회고

1. 고대하고 고대했던 나의 첫 어플리케이션. 그리고 첫 소감. 구현할 때는 잘 되던 동작이 테스트할 때는 휘청거리니... 애틋하다.

   ![image-20240111172459875](https://raw.githubusercontent.com/wholeheartedness/image_repo/main/img/image-20240111172459875.png)

---

2. MVC 패턴은 정말 흥미롭다. Model (Entity, Dto) - View - Controller (Controller, Repository, Service) 구조로 이렇게 긴 코드들이 상자에 담듯 쏙쏙 정리가 되니 말이다. 하지만 나는 이번 프로젝트에서 Entity를 DTO에 담지 않고 그대로 사용했다. 절대 금물이라고 했지만 사실 처음에 생각이 나지 않았다. 왠지 절대 들켜선 안되는 실수를 저지른 느낌... 앞으로 DTO 없는 프로젝트는 generate도 하지 말아야겠다

3. 🌟 그리고 사실.. 패턴 구조대로 패키지는 나누었으나 잘 활용하지 못했다. Entity별로 패키지 클래스와 인터페이스를 나누긴 했으나 처음에는 조그마한 게시판이니 이정도는 필요없겠지..하고 중간에 있는 Board 클래스만 믿었다. 그래서 Controller와 View는 아예 구분 없이 사용했다.

   사용하다보니 이게 이렇게 나누면 유지보수가 더 쉬웠겠다.. 하는 느낌을 받았다. 지금 당장 리팩토링 하라그러면 쩔쩔 매겠지만, 2년 후의 나는 가능하겠지

4. 🌟의존성이 중요한 스프링부트에서는 얼마나 애노테이션을 잘 활용하는지가 작업시간 단축에 핵심인 듯 하다. 마치 foreach 대신 한줄 한줄 별찍기를 하는 것 마냥 노가다 작업을 하면서 '이거 이러라고 있는 프레임워크가 아닌데...' 하는 자괴감이 조금 들었다. 

   의존성이라는 구조를 파악하기는 아직 버겁지만 어느정도 '쟤가 쟤를...아하...' 하는 느낌이 든다. 선구자들이 다져놓은 땅 밟는게 얼마나 행운인가. 더 공부하자

5. 의외로 View 관리가 참 힘들다. parameter를 준 것 같은데 모르겠다고 하고, 비슷한 저 View는 되는데 이건 왜 안되나 싶고. 특히 타임리프 구문이 너무 눈빠진다. `tr` 이라는 철자도 깨알 같고 `{(` 이거랑도 되게 비슷하게 생겨서 약간 뒷목 땡기는 기분. 하루 빨리 자바스크립트를 배우고싶어졌다.
6. GPT는 html을 정말 잘 짠다. 정말 오류 없이 잘 짠다. 메모장인지, 오류페이진지 헷갈릴 정도의 애플리케이션에 살을 붙이니 이렇게 그럴싸해보일수가? bootstrap 외에도 다른 프론트엔드 프레임워크가 궁금하다. 그리고 나도 배워서 GPT한테 좋은 소스를 주고 싶다. 하하
7. 마지막으로 쿼리문에 대해 (거의) 모르니 손댈 수 없이 소프트웨어에만 의존하는 나를 봤다. 앞으로도 워크벤치가 나의 쿼리를 쭉 짜주겠지만 GPT가 소스코드 짠다고 내가 몰라도 되는거 아니지 않나. 쿼리 공부 꼭 하자. 그리고 더 탄탄한 DB를 만들고 싶다. 더 견고한! 더 큰! DB에 내 데이터가 들어가 안착되어 있는게 이렇게 기분 좋은 일인가 싶다.
8. 혼자 공부하는 소중한 일주일을 보내게 되서, 그리고 얻어가는게 많아서 여러모로 재밌었다. 😄👏👏