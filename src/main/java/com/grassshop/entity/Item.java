package com.grassshop.entity;

import com.grassshop.constant.ItemStatus;
import com.grassshop.dto.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    public void updateItem(ItemFormDto itemFormDto) {
        this.id = itemFormDto.getId();
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemStatus = itemFormDto.getItemStatus();
        this.itemDetail = itemFormDto.getItemDetail();
    }
}
