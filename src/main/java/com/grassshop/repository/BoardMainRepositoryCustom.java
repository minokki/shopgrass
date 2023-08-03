package com.grassshop.repository;

import com.grassshop.dto.BoardSearchDto;
import com.grassshop.dto.MainBoardDto;
import com.grassshop.entity.BoardMain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardMainRepositoryCustom {

    Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
