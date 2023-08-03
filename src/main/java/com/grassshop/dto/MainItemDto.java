package com.grassshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    
    private String itemNm;
    
    private String itemDetail;
    
    private String imgUrl;
    
    private Integer price;

    @QueryProjection //querydsl 로 조회시 결과를 mainItemDto객체로 받아오도록 설정 / Q클래스 생성됨
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
