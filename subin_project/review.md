# 구현 방식 설명
- [Controller](#Controller)
- [Entity](#Entity클래스) 
- [DTO](#DTO클래스)
- [Service & Repository](#BoardService)

## Controller
컨트롤러 클래스 자체에 `@RequestMappting`과 `@RequiredArgsConstructor`을 추가해두었습니다.
```java
// Article의 Controller
@Controller
@RequestMapping("board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleController {
    //...
}
```
```java
// Comment의 Controller
@Controller
@RequestMapping("board/{boardId}/article/{articleId}/comment")
@RequiredArgsConstructor
public class CommentController { 
    //...
}
```
```java
// Board의 Controller
@Controller
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    //...
}
```
 이렇게 하면 Controller 내부 메서드의 `@RequestMapping`과 Controller의 `@RequestMapping`의 값을 합쳐서 URL이 구성됩니다.
만약 메서드의 매핑에 URL값을 전달하지 않으면 클래스의 URL만으로 매핑이됩니다.   
`@RequiredArgsConStructor`는 초기화 되지 않은 final 필드에 대해 생성자를 생성해 줍니다.   
 이때 클래스에 붙은 `@RequestMapping`에도 경로 변수를 작성해 줄 수 있으며 이를 메서드에서 매개변수에 할당하는 것도 가능합니다.
```java
@Controller
@RequestMapping("board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleController { 
    //...
    // 이 메서드는 '/board/{boardId}/article' 경로로의 요청에 호출됩니다. 
    @GetMapping
    public String article(
            @PathVariable("boardId")
            Long boardId,
            Model model) {
        model.addAttribute("articles", articleService.readAll());
        model.addAttribute("boardId", boardId);
        return String.format("board/%d/article/index", boardId);
    }
    //...
    
    // 이 메서드는 '/board/{boardId}/article/write' 경로로의 요청에 호출됩니다.
    @GetMapping("write")
    public String articleWrite(
            @PathVariable("boardId")
            Long boardId,
            Model model
    ) {
        model.addAttribute("boards", boardService.readAll());
        model.addAttribute("boardId", boardId);
        return "board/article/write";
    }
}
```
## Entity클래스
강의 중에 사용한 것과는 다르게 `@Data`를 사용하지 않고 전체 클래스에는 `@Getter`를, 속성별로 할당할 수 있는 값에는 `@Setter`를 적용해 주었습니다.   
또한 가변길이를 갖는 큰 데이터를 저장하기 위해 Article과 Comment의 content에는 @Lob을 적용해 주었습니다.
```java
@Getter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    @Lob
    private String content;

    @Setter
    private String originPassword;
    @OneToMany(mappedBy = "article")
    private final List<Comment> comments = new ArrayList<>();
    //...
}
```
## DTO클래스
실제로 데이터베이스에 저장되는 정보와 사용자가 서비스 활용을 위해서 필요로 하는 정보를 분리하기 위해서 DTO 클래스를 따로 정의하였습니다.
```java
// ArticleDto
@Getter
@ToString
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private String originPassword;
    private List<CommentDto> comments = new ArrayList<>();
}
```
`ArticleDto`클래스에는 `List<CommentDto>` 속성을 통해 댓글 목록을 확인할 수 있습니다.
```java
// Comment
@Getter
@ToString
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @Setter
    private String content;
    @Setter
    private String originPassword;
}
```
실제 서비스에서 댓글은 사실상 게시글의 부속처럼 활용되기 때문에, `CommentDto`에는 `Article`에 대한 정보가 포함되지 않습니다.
```java
// Board
@Getter
@NoArgsConstructor
public class BoardDto {
    private Long id;

    @Setter
    private String boardName;

    private List<ArticleDto> articles = new ArrayList<>();
}
```
`BoardDto`클래스에는 `List<ArticleDto>` 속성을 통해 게시글 목록을 확인할 수 있습니다.   
또한 DTO 클래스들은 Entity 클래스를 매개변수로 받는 정적 팩토리 메서닥 정의 되어 있습니다. 
```java
// static factory method
    public static ArticleDto fromEntity(Article entity) {
        ArticleDto dto = new ArticleDto();
        dto.id = entity.getId();
        dto.title = entity.getTitle();
        dto.content = entity.getContent().replace("\n", "<br>");
        dto.originPassword = entity.getOriginPassword();
        dto.comments = new ArrayList<>();
        for (Comment comment: entity.getComments())
            dto.comments.add(CommentDto.fromEntity(comment));
        return dto;
    }
```
이를 활용하면 실제로 DTO 객체가 필요한 시점에 `new`키워드를 이용해 생성하지 않고, Entity를 바탕으로 DTO를 만드는 과정을 간소화 할 수 있습니다.
```java
    public List<ArticleDto> readAll(){
        List<ArticleDto> articleList = new ArrayList<>();
        for (Article article : articleRepository.findAll()){
            articleList.add(ArticleDto.fromEntity(article));
        }
        return articleList;
    }
```
## Service & Repository
### BoardService
```java
//import ...
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardDto> readAll(){
        List<BoardDto> boards = new ArrayList<>();
        for (Board board: boardRepository.findAll())
            boards.add(BoardDto.fromEntity(board));
        return boards;
    }

    public BoardDto read(Long id){
        Board board = boardRepository.findById(id).orElseThrow();
        return BoardDto.fromEntity(board);
    }
}
```
게시판의 종류인 `자유게시판, 개발게시판, 일상게시판, 사건사고 게시판, 전체 게시글보기`는 sql을 통해 미리 insert해 두었기 때문에 `BoardService`에서는
각 게시판의 id를 통해 게시판 하나씩 읽는 메서드(`read`)와 게시판의 종류를 전부 볼 수 있는 메서드(`readAll`)을 만들어 두었습니다.   
- `readAll`: Repository의 `findAll()`을 통해서 저장되어있는 board들을 가져왔고 for each문을 통해 List인 boards에 담아 List를 리턴하여 Controller로 보내고 
이를 board의 index에 출력하도록 하였습니다.
- `read`: 기본적으로 `findById()` 메서드는 결과로 `optional`을 반환합니다. 여기서는 `optional`의 `orElseThrow()`를 사용해 존재하지 않는 데이터에 대해 예외가 발생하도록 하였습니다.

### ArticleService
```java
// 게시글 만들기
    public ArticleDto create(Long boardId,ArticleDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow();
        Article article = new Article(
                dto.getTitle(),
                dto.getContent(),
                dto.getOriginPassword(),
                board
        );
        // fromEntity -> entity를 dto로 변환
        return ArticleDto.fromEntity(articleRepository.save(article));
    }
```
`fromEntity`를 통해` create` 메서드의 결과로 DTO 클래스를 반환하여, 생성 이후에 만들어진 Entity를 확인할 수 있는 페이지로 이동할 수 있게 하였습니다.   
`save()`의 결과로 만들어진 Entity를 활용합니다.

```java
 // 게시글 하나 읽기
    public ArticleDto readOne(Long id){
        Article article = articleRepository.findById(id).orElseThrow();
        return ArticleDto.fromEntity(article);
    }
```
기본적으로 `findById()` 메서드는 결과로 `optional`을 반환합니다. 여기서는 `optional`의 `orElseThrow()`를 사용해 존재하지 않는 데이터에 대해 예외가 발생하도록 하였습니다.

```java
 // 게시글 모두 읽기
    public List<ArticleDto> readAll(){
        List<ArticleDto> articleList = new ArrayList<>();
        for (Article article : articleRepository.findAll()){
            articleList.add(ArticleDto.fromEntity(article));
        }
        return articleList;
    }
```
Repository의 `findAll()`을 통해서 저장되어있는 article을 가져왔고 for each문을 통해 List인 articleList에 담아 List를 리턴하여 Controller로 보내고 
이를 article의 index에 출력하도록 하였습니다.

```java
   // 게시글 수정
    public ArticleDto update(Long id, ArticleDto dto){
        Article article = articleRepository.findById(id).orElseThrow();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setOriginPassword(dto.getOriginPassword());
        return ArticleDto.fromEntity(articleRepository.save(article));
    }
```
게시글 하나 읽어오는 메서드인 `readOne`처럼 게시글을 하나 업데이트 하기 위해서는 수정하려고 하는 게시글의 id를 통해 읽어와야 합니다. 
`findById()` 메서드를 통해 읽어온 객체를 새 article 객체를 만들어 사용자가 수정한 정보를 get으로 받아와서 set으로 article에 저장해 줍니다.
`save()`를 통해 repository에 저장하고 `fromEntuty`를 통해 `update` 메서드의 결과로 DTO 클래스를 반환합니다. 
이후에는 업데이트된 Entity를 확인할 수있는 페이지로 이동할 수 있게 하였습니다.

```java
  // 게시글 삭제
   public void delete(Long id){
       articleRepository.delete(articleRepository.findById(id).orElseThrow());
   }
```
삭제하고자 하는 게시글의 id를 받아와 `findById()`를 통해 객체를 찾고 `delete()`를 통해 repository에 저장된 객체를 삭제합니다.
삭제된 이후에는 삭제된 게시물이 있던 게시판의 게시글 목록으로 이동할 수 있도록 하였습니다. 

### CommentService
```java
    public CommentDto create(Long boardId, Long articleId, CommentDto dto) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        Article article = articleRepository.findById(articleId)
                .orElseThrow();
        Comment comment = new Comment(
                dto.getContent(),
                dto.getOriginPassword(),
                article,
                board
        );
        return CommentDto.fromEntity(commentRepository.save(comment));
    }
```
`fromEntity`를 통해` create` 메서드의 결과로 DTO 클래스를 반환하여, 생성 이후에 만들어진 Entity를 확인할 수 있는 페이지로 이동할 수 있게 하였습니다.   
`save()`의 결과로 만들어진 Entity를 활용합니다.

```java
    public CommentDto read(Long commentId) {
        return CommentDto.fromEntity(commentRepository.findById(commentId)
                .orElseThrow());
    }
```
Comment를 하나씩 읽기 위한 메서드로 `findById()` 메서드는 결과로 `optional`을 반환합니다. 
여기서는 `optional`의 `orElseThrow()`를 사용해 존재하지 않는 데이터에 대해 예외가 발생하도록 하였습니다.

```java
    public CommentDto update(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(dto.getContent());
        comment.setOriginPassword(dto.getOriginPassword());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

```
article과 마찬가지로 comment 하나를 업데이트 하기 위해선 업데이트 하고자 하는 comment의 id를 통해 읽어온 뒤 새로운 객체(comment)를 만들어 저장합니다. 
사용자가 입력한 값들을 get을 통해 가져온뒤 객체에 set을 통해 저장하고 repository의 `save()` 메서드를 통해 repository에 다시 저장합니다.

```java
   public void delete(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(dto.getContent());
        comment.setOriginPassword(dto.getOriginPassword());
        commentRepository.delete(comment);

    }
```
repository의 `delete()` 메서드를 통해 repository에서 해당 id를 가진 comment를 삭제합니다.
