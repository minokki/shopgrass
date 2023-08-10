package com.grassshop.dto;

import com.grassshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearchDto {
    //현재시간과 상품일 비교해서 데이터조회
    private String searchDateType;
    //판매상태 기준으로 조회
    private OrderStatus orderStatus;
    //유형 조회 (상품명, 상품등록자아이디)
    private String searchBy;
    //조회할 검색어 저장할 변수
    private String searchQuery;

}
