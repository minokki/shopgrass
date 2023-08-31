package com.grassshop.entity;

import com.grassshop.dto.QnaFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "qna")
public class Qna extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    public void updateQna(QnaFormDto qnaFormDto) {
        this.title = qnaFormDto.getTitle();
        this.content = qnaFormDto.getContent();
    }

}
