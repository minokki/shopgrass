package com.grassshop.dto;

import com.grassshop.entity.Ntc;
import com.grassshop.entity.Qna;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QnaFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 값")
    private String title;

    @NotBlank(message = "내용은 필수 값")
    private String content;

    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    private Long views;

    private String createBy;

    private static ModelMapper modelMapper = new ModelMapper();

    public Qna createQna(){
        Qna qna = modelMapper.map(this, Qna.class);
        qna.setViews(0L); // views를 0으로 초기화
        return qna;
    }

}
