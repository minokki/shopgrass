package com.grassshop.repository;

import com.grassshop.entity.BoardMainImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMainImgRepository extends JpaRepository<BoardMainImg, Long> {

    List<BoardMainImg> findByBoardMainIdOrderByIdAsc(Long boardMainId);

}
