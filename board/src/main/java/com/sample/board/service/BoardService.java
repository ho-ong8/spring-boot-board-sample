package com.sample.board.service;

import com.sample.board.dto.BoardDTO;
import com.sample.board.entity.BoardEntity;
import com.sample.board.entity.BoardFileEntity;
import com.sample.board.repository.BoardFileRepository;
import com.sample.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 게시글 작성
    public void write(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else {
            /*
            // 단일 파일 첨부
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            String savePath = "C:/board_file/" + storedFileName;
            boardFile.transferTo(new File(savePath));
            BoardEntity boardEntity = BoardEntity.toBoardFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);
            */

            // 다중 파일 첨부
            BoardEntity boardEntity = BoardEntity.toBoardFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();

            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/board_file/" + storedFileName;
                boardFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    // 게시글 목록
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    // 게시글 조회
    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> board = boardRepository.findById(id);

        if (board.isPresent()) {
            return BoardDTO.toBoardDTO(board.get());
        } else {
            return null;
        }
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 게시글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 게시글 수정
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    // 게시글 페이징
    public Page<BoardDTO> paging(Pageable pageable) {
        // 페이지 번호 (0부터 시작)
        int page = pageable.getPageNumber() - 1;
        // 페이지 게시글 수
        int pageLimit = 3;

        // 게시글 목록
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")
        ));

        Page<BoardDTO> boardDTOPage = boardEntityPage.map(board -> new BoardDTO(
                board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()
        ));

        return boardDTOPage;
    }

}
