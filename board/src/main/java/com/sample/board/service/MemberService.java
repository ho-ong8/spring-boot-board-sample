package com.sample.board.service;

import com.sample.board.dto.MemberDTO;
import com.sample.board.entity.MemberEntity;
import com.sample.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    public void join(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }
    
    // 로그인
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());

        if (memberEmail.isPresent()) {
            MemberEntity memberEntity = memberEmail.get();

            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                MemberDTO member = MemberDTO.toMemberDTO(memberEntity);
                return member;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
