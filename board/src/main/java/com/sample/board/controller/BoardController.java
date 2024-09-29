package com.sample.board.controller;

import com.sample.board.dto.BoardDTO;
import com.sample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @GetMapping("/write")
    public String writeForm() {
        return "/board/write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO boardDTO) {
        boardService.write(boardDTO);
        return "redirect:/board/paging";
    }

    // 게시글 목록
    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "/board/list";
    }

    // 게시글 조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,
                           @PageableDefault(page=1) Pageable pageable,
                           Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "/board/detail";
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    // 게시글 수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "/board/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO,
                         @PageableDefault(page=1) Pageable pageable,
                         Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        model.addAttribute("page", pageable.getPageNumber());
        return "/board/detail";
    }

    // 게시글 페이징
    @GetMapping("/paging")
    public String paging(@PageableDefault(page=1) Pageable pageable,
                         Model model) {
        Page<BoardDTO> boardDTOPage = boardService.paging(pageable);

        // 페이지 번호 수
        int pageLimit = 3;
        // 시작 페이지 (1, 4, 7, 10, ...)
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / pageLimit))) - 1) * pageLimit + 1;
        // 마지막 페이지 (3, 6, 9, 12, ...)
        int endPage = startPage + pageLimit - 1 < boardDTOPage.getTotalPages() ? startPage + pageLimit - 1 : boardDTOPage.getTotalPages();

        model.addAttribute("boardList", boardDTOPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/paging";
    }

}
