package com.grassshop.dto;

import com.grassshop.constant.ItemStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    //현재시간과 상품일 비교해서 데이터조회
    private String searchDateType;

    //판매상태 기준으로 조회
    private ItemStatus itemStatus;

    //유형 조회 (상푸명, 상품등록자아이디)
    private String searchBy;

    //조회할 검색어 저장할 변수
    private String searchQuery;

}
