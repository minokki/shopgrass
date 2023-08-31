package com.grassshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaSearchDto {
    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";
}
