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

    /* 시공사례 등록 */
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

    /* 시공사례 조회 */
    @Transactional(readOnly = true)
    public BoardMainFormDto getBoardMainDtl(Long boardMainId) {
        // boardMainId에 해당하는 BoardMain 엔티티 조회
        BoardMain boardMain = boardMainRepository.findById(boardMainId).orElseThrow(EntityNotFoundException::new);

        // boardMainId에 해당하는 BoardMainImg 엔티티들 조회
        List<BoardMainImg> boardMainImgList = boardMainImgRepository.findByBoardMainIdOrderByIdAsc(boardMainId);

        // BoardMainFormDto에 필요한 정보 설정
        BoardMainFormDto boardMainFormDto = new BoardMainFormDto();
        boardMainFormDto.setId(boardMain.getId());
        boardMainFormDto.setTitle(boardMain.getTitle());
        boardMainFormDto.setContent(boardMain.getContent());
        boardMainFormDto.setCreateDate(boardMain.getRegTime());
        boardMainFormDto.setCreateBy(boardMain.getCreateBy());

        // BoardMainImg 엔티티의 example 값을 기준으로 이미지들을 분류하여 설정
        for (BoardMainImg boardMainImg : boardMainImgList) {
            BoardMainImgDto boardMainImgDto = BoardMainImgDto.ofv(boardMainImg);
            if (Example.BEFORE.equals(boardMainImg.getExample())) {
                boardMainImgDto.setBeforeImgUrl(boardMainImg.getImgUrl());
                boardMainFormDto.setBeforeImgUrl(boardMainImg.getImgUrl());
            } else if (Example.AFTER.equals(boardMainImg.getExample())) {
                boardMainImgDto.setAfterImgUrl(boardMainImg.getImgUrl());
                boardMainFormDto.setAfterImgUrl(boardMainImg.getImgUrl());
            }
            // 이미지 정보를 리스트에 추가
            boardMainFormDto.getBoardMainImgDtoList().add(boardMainImgDto);
        }
        return boardMainFormDto;
    }

    /* 시공사례 UPDATE */
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

    /* 시공사례 PAGING(관리자) */
    public Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardMainRepository.getAdminBoardMainPage(boardSearchDto, pageable);
    }

    /* 시공사례 PAGING */
    @Transactional(readOnly = true)
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardMainRepository.getMainBoardPage(boardSearchDto, pageable);
    }
}
