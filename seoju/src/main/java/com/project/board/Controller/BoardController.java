package com.project.board.Controller;

import com.project.board.Service.BoardCategoryService;
import com.project.board.Service.BoardService;
import com.project.board.Service.CommentService;
import com.project.board.Service.HashtagService;
import com.project.board.entity.Board;
import com.project.board.entity.BoardCategory;
import com.project.board.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardCategoryService boardCategoryService;
    @Autowired
    private CommentService commentService;
    private final HashtagService hashtagService;

    @Autowired
    public BoardController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }


    ////////////////////////////////////////////////////CREATE////////////////////////////////////////////////////
    // 👇글작성 폼 호출
    @GetMapping("/board/write")
    public String boardWriteForm(Model model) {
        List<BoardCategory> categories = boardCategoryService.categoriList();
        model.addAttribute("categories", categories);
        return "boardWrite";
    }

    // 👇글작성 폼 제출
    @PostMapping("/board/writesub")
//    public String boardWriteSub(@RequestParam("title") String title,
//                                @RequestParam("content") String content) {
    public String boardWriteSub(@ModelAttribute Board board, Model model,
                                @RequestParam(name = "categoryid") Integer categoryid,
                                @RequestParam(value = "file", required = false) MultipartFile file) throws Exception { //required = false : 게시글이 반드시 첨부되지 않아도 됨
        boardService.writeService(board, file, categoryid);
        model.addAttribute("message", "글이 작성됐다냥😺✍️"); // Model에 "message" 속성 추가
        model.addAttribute("searchUrl", "/board/list"); // Model에 "searchUrl" 속성 추가
        return "message"; // "message"와 "searchUrl"을 가지고 있는 모델을 message라는 뷰로 return
    }

    // 👇 댓글작성 폼 제출
    @PostMapping("/board/addComment")
    public String commentWriteSub(@ModelAttribute Comment comment, Model model,
                                  @RequestParam(name = "boardid") Integer boardid) {
        commentService.writeComment(comment, boardid);
        return "redirect:/board/view/" + boardid;
    }

    ////////////////////////////////////////////////////READ////////////////////////////////////////////////////
    // 👇 홈화면 조회
    @GetMapping("/home")
    public String home(Model model) {
        List<BoardCategory> categories = boardCategoryService.categoriList();
        model.addAttribute("categories", categories);
        return "home";
    }


    // 👇전체 게시글 리스트 조회
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
                            @RequestParam(name = "searchType", required = false) String searchType) {
        List<BoardCategory> categories = boardCategoryService.categoriList();
        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchType, searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1; // 0부터 시작하기때문에 1을 더해줌
        int startPage = Math.max(nowPage - 5, 1); // 두 값 중 높은 값을 반환하니까 1보다 작으면 1을 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 두 값 중 낮은 값을 반환하니까 Total page보다 작을때만 nowPage + 5를 반환


        //이 변수들을 다 넘겨줌
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("categories", categories);
        return "boardListAll";
    }

    // 👇 게시판 별 리스트 조회
    @GetMapping("/board/list/{categoryid}")
    public String boardListByCategory(Model model,
                                      @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                      @PathVariable(name = "categoryid") Integer categoryid,
                                      @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
                                      @RequestParam(name = "searchType", required = false) String searchType) {
        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardListByCategory(categoryid, pageable);
        } else {
            list = boardService.boardSearchListByCategory(categoryid, searchType, searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 5, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        String categoryName = boardCategoryService.categoryName(categoryid);


        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardCategory", categoryid); // 카테고리 정보를 모델에 추가
        model.addAttribute("categoryName", categoryName);
        System.out.println(categoryName);


        return "boardListCategory";
//        } catch (Exception e) {
//            // 예외 처리: 존재하지 않는 카테고리 ID에 대한 경우
//            model.addAttribute("error", "들어갈 박스가 없어요");
//            return "errorPage"; // 적절한 에러 페이지로 이동하도록 설정
//        }

    }

    // 👇단일 게시글 조회
    @GetMapping("/board/view/{id}")
    public String boardView(Model model,
                            @PathVariable("id") Integer id
    ) {

        model.addAttribute("board", boardService.getContentById(id));
        return "boardView";
    }

    ////////////////////////////////////////////////////DELETE////////////////////////////////////////////////////
    // 👇게시글 삭제 폼 호출
    @GetMapping("/board/delete/{id}")
    public String getBoardDelete(Model model,
                                 @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.getContentById(id));
        return "boardDelete";
    }

    // 👇게시글 삭제 폼 제출

    @PostMapping("/board/delete/{id}")
    public String postBoardDelete(Model model,
                                  @PathVariable("id") Integer id,
                                  @RequestParam("password") String inputPassword) {
        boolean passwordMatches = boardService.checkPassword(boardService.getContentById(id).getId(), inputPassword);
        if (!passwordMatches) {
            // 비밀번호가 일치하지 않으면 처리
            model.addAttribute("message", "비밀번호가 틀렸다냥😿");
            model.addAttribute("searchUrl", "/board/view/" + id);
            return "message";
        }

        // 비밀번호가 일치하면 삭제 진행
        boardService.boardDelete(boardService.getContentById(id).getId());

        model.addAttribute("message", "글이 삭제됐다냥😺🔥");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    // 👇댓글 삭제 폼 호출
    @GetMapping("/board/deleteComment/{id}")
    public String getCommentDelete(Model model,
                                   @PathVariable("id") Integer id) {
        model.addAttribute("comment", commentService.commentView(id));
        return "boardCommentDelete";
    }

    // 👇댓글 삭제 폼 제출

    @PostMapping("/board/deleteComment/{id}")
    public String postCommentDelete(Model model,
                                    @PathVariable("id") Integer id,
                                    @RequestParam("password") String inputPassword) {
        int board_id = commentService.commentView(id).getBoard().getId();
        boolean passwordMatches = commentService.checkPassword(commentService.commentView(id).getId(), inputPassword);
        if (!passwordMatches) {
            // 비밀번호가 일치하지 않으면 처리
            model.addAttribute("message", "비밀번호가 틀렸다냥😿");
            model.addAttribute("searchUrl", "/board/view/" + board_id);
            return "message";
        }

        // 비밀번호가 일치하면 삭제 진행
        commentService.deleteComment(commentService.commentView(id).getId());

        model.addAttribute("message", "댓글이 삭제됐다냥😺🔥");
        model.addAttribute("searchUrl", "/board/view/" + board_id);
        return "message";
    }

    ////////////////////////////////////////////////////UPDATE////////////////////////////////////////////////////
    // 👇글 수정 폼 호출
    @GetMapping("/board/modify/{id}")
    public String boardModify(Model model,
                              @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.getContentById(id));
        return "boardModify";
    }

    // 👇글 수정 폼 제출 (엔티티 새로 생성)
    /*
    @PostMapping("/board/update/{id}")
    // 여기 매개변수 board는 새로 가져온 내용
    public String boardUpdate(@PathVariable("id") Integer id, Board board) {
        Board boardTemp = boardService.boardView(id); // 기존에 있던 글이 담겨 올 것
        boardTemp.setTitle(board.getTitle()); // 수정할 제목 가져와
        boardTemp.setContent(board.getContent()); // 수정할 내용 가져와
        boardService.writeService(boardTemp);
        return "redirect:/board/list";
     */

    // 👇글 수정 폼 제출 (트랜잭션을 사용한 변경감지)
    @Transactional
    @PostMapping("/board/update/{id}")
    public String boardUpdate(Board updatedBoard, Model model,
                              @PathVariable("id") Integer id,
                              @RequestParam("password") String inputPassword) {
        boolean passwordMatches = boardService.checkPassword(boardService.getContentById(id).getId(), inputPassword);
        System.out.println("해당 id :" + boardService.getContentById(id).getId());
        System.out.println("해당 pw :" + boardService.getContentById(id).getPassword());
        System.out.println("입력 pw :" + inputPassword);
        if (!passwordMatches) {
            // 비밀번호가 일치하지 않으면 처리
            model.addAttribute("message", "비밀번호가 틀렸다냥😿");
            model.addAttribute("searchUrl", "/board/view/" + id);
            return "message";
        }
        // 데이터베이스에서 기존 엔터티를 가져옴
        Board existingBoard = boardService.getContentById(id);
        // 변경 감지를 통해 수정할 필드만 업데이트
        existingBoard.setTitle(updatedBoard.getTitle());
        existingBoard.setContent(updatedBoard.getContent());
        existingBoard.setFilename(updatedBoard.getFilename());
        existingBoard.setFilepath(updatedBoard.getFilepath());
        // 이미 JPA가 변경 감지로 인해 업데이트가 이뤄지므로 별도의 저장 메서드 호출이 필요하지 않음
        model.addAttribute("message", "글이 수정됐다냥😺🛠️"); // Model에 "message" 속성 추가
        model.addAttribute("searchUrl", "/board/list"); // Model에 "searchUrl" 속성 추가
        return "message"; // "m
    }


//    ////////////////////////////////////// 해쉬태그 /////////////////////////////////
//    @GetMapping("/hashtags/{id}")
//    public List<String> getHashtags(@PathVariable Integer id) {
//        // 게시물 ID로 내용을 가져온다고 가정
//        String content = boardService.getContentById(id).getContent();
//
//        // 서비스 계층의 메서드를 호출하여 해시태그를 추출
//        return hashtagService.getHashtags(content);
//    }

}
