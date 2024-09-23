package com.sample.board.service;

import com.sample.board.dto.MemberDTO;
import com.sample.board.entity.MemberEntity;
import com.sample.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    // 회원목록
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }

        return memberDTOList;
    }

    // 회원정보 조회
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> member = memberRepository.findById(id);

        if (member.isPresent()) {
            return MemberDTO.toMemberDTO(member.get());
        } else {
            return null;
        }
    }

    // 회원정보 삭제
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

}
