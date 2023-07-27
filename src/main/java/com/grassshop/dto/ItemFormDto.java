package com.grassshop.dto;

import com.grassshop.constant.ItemStatus;
import com.grassshop.entity.Item;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFormDto {
    
    private Long id;
    
    @NotBlank(message = "상품명은 필수")
    private String itemNm;
    
    @NotNull(message = "가격은 필수")
    private Integer price;
    
    @NotBlank(message = "설명은 필수")
    private String itemDetail;

    @NotNull(message = "재고는 필수")
    private Integer stockNumber;

    private ItemStatus itemStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지

    private List<Long> itemImgIds = new ArrayList<>(); //수정시 아이디 담아둘 용도

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }




}
