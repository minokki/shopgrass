package com.grassshop.repository;

import com.grassshop.dto.QnaSearchDto;
import com.grassshop.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepositoryCustom {
    Page<Qna> getQnaPage(QnaSearchDto qnaSearchDto, Pageable pageable);
}
