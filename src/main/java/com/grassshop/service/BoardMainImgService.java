package com.grassshop.service;

import com.grassshop.constant.Example;
import com.grassshop.entity.BoardMainImg;
import com.grassshop.repository.BoardMainImgRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardMainImgService {
    @Value("${boardMainLocation}")
    private String boardMainLocation;

    private final BoardMainImgRepository boardMainImgRepository;

    private final FileService fileService;

    public void saveBoardMainImg(BoardMainImg boardMainImg, MultipartFile multipartFile) throws Exception {
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.exampleUploadFile(boardMainLocation, oriImgName, multipartFile.getBytes());
            imgUrl = "/images/board/" + imgName;
        }

        //상품 이미지 정보 저장
        boardMainImg.updateBoardMainImg(oriImgName, imgName, imgUrl); //업데이트
        boardMainImgRepository.save(boardMainImg);
    }

    public void updateBoardMainImg(Long boardMainImgId, MultipartFile multipartFile) throws Exception{
        if (!multipartFile.isEmpty()) {
            BoardMainImg savedBoardMainImg = boardMainImgRepository.findById(boardMainImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 삭제
            if (!StringUtils.isEmpty(savedBoardMainImg.getImgName())){
                fileService.deleteFile(boardMainLocation + "/" +
                        savedBoardMainImg.getOriImgName());
            }

            String oriImgName = multipartFile.getOriginalFilename();
            String imgName = fileService.exampleUploadFile(boardMainLocation, oriImgName, multipartFile.getBytes());
            String imgUrl = "/images/board/" + imgName;
            savedBoardMainImg.updateBoardMainImg(oriImgName,imgName,imgUrl);
        }
    }


}
