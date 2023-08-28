package com.grassshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Community_ntc")
@Getter
@Setter
@ToString
public class CommunityNtc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "community_ntc_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;
}
