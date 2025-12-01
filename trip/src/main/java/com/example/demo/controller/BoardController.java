package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.Continent;
import com.example.demo.dto.Tag;
import com.example.demo.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor

public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }

    // 메인 페이지: 전체 게시글 목록 조회 (필터 지원)
    @GetMapping("/posts")
    public String getAllPosts(@RequestParam(name = "continentId", required = false) Integer continentId,
                              @RequestParam(name = "tagId", required = false) Integer tagId,
                              Model model) {
        List<BoardDTO> posts = boardService.getFilteredPosts(continentId, tagId);
        model.addAttribute("posts", posts);

        List<Continent> continents = boardService.findAllContinents();
        model.addAttribute("continents", continents);

        List<Tag> tags = boardService.findAllTags();
        model.addAttribute("tags", tags);

        return "postList";
    }

    // 상세 보기
    @GetMapping("/posts/{id}")
    public String getPostDetail(@PathVariable("id") Integer id, Model model) {
        BoardDTO post = boardService.getPostDetail(id);
        model.addAttribute("post", post);
        return "detailPost";
    }

    // 게시글 작성폼 호출
    @GetMapping("/posts/new") 
    public String newPostForm(Model model) {
        
        model.addAttribute("continents", boardService.findAllContinents());
        model.addAttribute("tags", boardService.findAllTags());
        model.addAttribute("countries", boardService.findAllCountries());
        
        return "addPost";
    }


    // 게시글 작성 처리
    @PostMapping("/posts")
    public String createPost(BoardDTO boardDTO) {
        // 실제 DB 저장 로직 수행
        boardService.createPost(boardDTO);
        
        // 저장 후 목록 페이지로 리다이렉트하여 중복 전송을 막습니다.
        return "redirect:/posts"; 
    }
    
    // 게시글 삭제 처리 (POST) - 비밀번호 검사
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") Integer id,
                             @RequestParam("password") String password,
                             Model model) {
        // 비밀번호 확인
        boolean ok = boardService.isPasswordValid(id, password);
        if (!ok) {
            // 상세 페이지로 돌아가면서 에러 메시지 노출
            BoardDTO post = boardService.getPostDetail(id);
            model.addAttribute("post", post);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "detailPost";
        }

        // 서비스에서 트랜잭션으로 태그 매핑 삭제 및 게시글 삭제 처리
        boardService.removePost(id);
        return "redirect:/posts";
    }

    // 게시글 수정 폼 호출
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable("id") Integer id, Model model) {
        BoardDTO post = boardService.getPostDetail(id);
        model.addAttribute("post", post);

        model.addAttribute("continents", boardService.findAllContinents());
        model.addAttribute("tags", boardService.findAllTags());
        model.addAttribute("countries", boardService.findAllCountries());

        return "editPost";
    }

    // 게시글 수정 처리 - 비밀번호 검사
    @PostMapping("/posts/{id}/edit")
    public String updatePost(BoardDTO boardDTO, Model model) {
        Integer id = boardDTO.getPostId();
        String provided = boardDTO.getPassword();
        if (!boardService.isPasswordValid(id, provided)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("post", boardDTO);
            return "editPost";
        }

        boardService.updatePost(boardDTO);
        boardDTO = boardService.getPostDetail(boardDTO.getPostId());
        model.addAttribute("post", boardDTO);
        return "detailPost";
    }
}