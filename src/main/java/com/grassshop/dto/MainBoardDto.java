package com.grassshop.dto;

import com.grassshop.constant.Example;
import com.grassshop.entity.BoardMainImg;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter


public class MainBoardDto {
    private Long id;
    private String title;
    private String content;
    private String imgUrlBefore;
    private String imgUrlAfter;

    // 기본 생성자
    public MainBoardDto() {
    }

    @QueryProjection
    public MainBoardDto(Long id, String title, String content, String imgUrlBefore, String imgUrlAfter) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrlBefore = imgUrlBefore;
        this.imgUrlAfter = imgUrlAfter;
    }
}