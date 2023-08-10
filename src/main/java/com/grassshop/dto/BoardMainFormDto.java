package com.grassshop.dto;

import com.grassshop.entity.BoardMain;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class BoardMainFormDto {
    private Long id;
    
    @NotBlank(message = "제목은필수")
    private String title;

    @NotBlank(message = "내용은필수")
    private String content;

    private String imgUrl;

    private String afterImgUrl; // after 이미지의 URL

    private String beforeImgUrl; // before 이미지의 URL

    //게시글 수정시 이미지 정보 저장하는 리스트
    private List<BoardMainImgDto> boardMainImgDtoList = new ArrayList<>();

    //게시글 이미지 id 저장하는 리스트
    private List<Long> boardMainImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public BoardMain createBoardMain(){
        return modelMapper.map(this, BoardMain.class);
    }

    public static BoardMainFormDto ofv(BoardMain boardMain) {
        return modelMapper.map(boardMain, BoardMainFormDto.class);
    }
}
