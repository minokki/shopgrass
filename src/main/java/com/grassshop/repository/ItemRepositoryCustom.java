package com.grassshop.repository;


import com.grassshop.dto.ItemSearchDto;
import com.grassshop.dto.MainItemDto;
import com.grassshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable); //상품리스트 가져오는 메소드

}
