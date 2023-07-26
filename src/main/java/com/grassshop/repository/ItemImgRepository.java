package com.grassshop.repository;

import com.grassshop.domain.Item;
import com.grassshop.domain.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
