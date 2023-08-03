package com.grassshop.service;

import com.grassshop.constant.Example;
import com.grassshop.dto.BoardMainFormDto;
import com.grassshop.dto.BoardMainImgDto;
import com.grassshop.dto.BoardSearchDto;
import com.grassshop.dto.MainBoardDto;
import com.grassshop.entity.BoardMain;
import com.grassshop.entity.BoardMainImg;
import com.grassshop.repository.BoardMainImgRepository;
import com.grassshop.repository.BoardMainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardMainService {

    private final BoardMainImgRepository boardMainImgRepository;
    private final BoardMainRepository boardMainRepository;
    private final BoardMainImgService boardMainImgService;

    public Long saveBoardMain(BoardMainFormDto boardMainFormDto, List<MultipartFile> multipartFiles) throws Exception {

        //게시글 등록
        BoardMain boardMain = boardMainFormDto.createBoardMain();
        boardMainRepository.save(boardMain);

        //이미지 등록
        for (int i = 0; i < multipartFiles.size(); i++) {
            BoardMainImg boardMainImg = new BoardMainImg();
            boardMainImg.setBoardMain(boardMain);
            if (i == 0) {
                boardMainImg.setExample(Example.BEFORE);  //첫번재 사진 BEFORE
            } else {
                boardMainImg.setExample(Example.AFTER); //두번째 사진 AFTER
            }
            boardMainImgService.saveBoardMainImg(boardMainImg, multipartFiles.get(i));
        }
        return boardMain.getId();
    }

    @Transactional(readOnly = true)
    public BoardMainFormDto getBoardMainDtl(Long boardMainId) {

        List<BoardMainImg> boardMainImgList = boardMainImgRepository.findByBoardMainIdOrderByIdAsc(boardMainId);

        List<BoardMainImgDto> boardMainImgDtoList = new ArrayList<>();
        for (BoardMainImg boardMainImg : boardMainImgList) {
            BoardMainImgDto boardMainImgDto = BoardMainImgDto.ofv(boardMainImg);
            boardMainImgDtoList.add(boardMainImgDto);
        }

        BoardMain boardMain = boardMainRepository.findById(boardMainId).orElseThrow(EntityNotFoundException::new);
        BoardMainFormDto boardMainFormDto = BoardMainFormDto.ofv(boardMain);
        boardMainFormDto.setBoardMainImgDtoList(boardMainImgDtoList);
        return boardMainFormDto;
    }

    public Long updateBoardMain(BoardMainFormDto boardMainFormDto, List<MultipartFile> multipartFiles) throws Exception {

        BoardMain boardMain = boardMainRepository.findById(boardMainFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        boardMain.updateBoardMain(boardMainFormDto);
        List<Long> boardMainImgIds = boardMainFormDto.getBoardMainImgIds();

        //이미지 등록
        for (int i = 0; i < multipartFiles.size(); i++) {

            boardMainImgService.updateBoardMainImg(boardMainImgIds.get(i), multipartFiles.get(i));
        }
        return boardMain.getId();
    }

    public Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardMainRepository.getAdminBoardMainPage(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardMainRepository.getMainBoardPage(boardSearchDto, pageable);
    }
}
