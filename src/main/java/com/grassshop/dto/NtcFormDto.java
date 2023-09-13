package com.grassshop.dto;

import com.grassshop.entity.Ntc;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class NtcFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수")
    private String title;

    @NotBlank(message = "내용은 필수")
    private String content;

    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    private String createBy;

    private String isImportant;

    private static ModelMapper modelMapper = new ModelMapper();

    public Ntc createNtc(){
        return modelMapper.map(this, Ntc.class);
    }
}
