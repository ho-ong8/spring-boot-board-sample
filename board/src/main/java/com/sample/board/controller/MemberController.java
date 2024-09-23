package com.sample.board.controller;

import com.sample.board.dto.MemberDTO;
import com.sample.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @GetMapping("/join")
    public String joinForm() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDTO memberDTO) {
        memberService.join(memberDTO);
        return "/member/login";
    }

    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO member = memberService.login(memberDTO);

        if (member != null) {
            session.setAttribute("member", member.getMemberEmail());
            return "main";
        } else {
            return "index";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 회원목록
    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "/member/list";
    }

    // 회원정보 조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "/member/detail";
    }

    // 회원정보 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/member/";
    }

    // 회원정보 수정
    @GetMapping("/update")
    public String updateForm(Model model, HttpSession session) {
        String member = (String) session.getAttribute("member");
        MemberDTO memberDTO = memberService.findByMemberEmail(member);
        model.addAttribute("member", memberDTO);
        return "/member/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    // 이메일 중복체크
    @PostMapping("/check-email")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        return memberService.emailCheck(memberEmail);
    }

}
