package com.grassshop.entity;

import com.grassshop.constant.Example;
import com.grassshop.constant.Role;
import com.grassshop.dto.BoardMainFormDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "board_main")
@Getter
@Setter
@ToString
public class BoardMain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_main_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long views = 0L;

    public void updateBoardMain(BoardMainFormDto boardMainFormDto) {
        this.title = boardMainFormDto.getTitle();
        this.content = boardMainFormDto.getContent();
    }
}
