package com.sample.board.service;

import com.sample.board.dto.CommentDTO;
import com.sample.board.entity.BoardEntity;
import com.sample.board.entity.CommentEntity;
import com.sample.board.repository.BoardRepository;
import com.sample.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 작성
    public Long write(CommentDTO commentDTO) {
        Optional<BoardEntity> board = boardRepository.findById(commentDTO.getBoardId());

        if (board.isPresent()) {
            BoardEntity boardEntity = board.get();
            CommentEntity commentEntity = CommentEntity.toCommentEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    // 댓글 목록
    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }

}
