package com.sample.board.controller;

import com.sample.board.dto.BoardDTO;
import com.sample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
        return "redirect:/board/";
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
    public String findById(@PathVariable Long id, Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "/board/detail";
    }

}
