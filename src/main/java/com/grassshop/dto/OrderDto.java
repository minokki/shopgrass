package com.grassshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private Long itemId;

    @Min(value = 1,message = "최수 주문수량은 1개입니다.")
    @Max(value = 999,message = "최대 주문수량은 999개입니다.")
    private int count;
}
