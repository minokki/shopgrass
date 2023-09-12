package com.grassshop.service;

import com.grassshop.dto.QnaFormDto;
import com.grassshop.dto.QnaResponseDto;
import com.grassshop.dto.QnaSearchDto;
import com.grassshop.entity.Qna;
import com.grassshop.repository.QnaRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;

    public Long saveCommunityQna(QnaFormDto qnaFormDto) {
        Qna qna = qnaFormDto.createQna();
        qnaRepository.save(qna);
        return qna.getId();
    }


    @Transactional(readOnly = true)
    public QnaFormDto getCommunityQna(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);

        QnaFormDto qnaFormDto = new QnaFormDto();
        qnaFormDto.setId(qna.getId());
        qnaFormDto.setTitle(qna.getTitle());
        qnaFormDto.setContent(qna.getContent());
        qnaFormDto.setCreateDate(qna.getRegTime());
        qnaFormDto.setCreateBy(qna.getCreateBy());

        return qnaFormDto;
    }

    public Long updateCommunityQna(QnaFormDto qnaFormDto) {
        Qna qna = qnaRepository.findById(qnaFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        qna.updateQna(qnaFormDto);
        return qna.getId();
    }

    @Transactional(readOnly = true)
    public Page<Qna> getQnaPage(QnaSearchDto qnaSearchDto, Pageable pageable) {
        return qnaRepository.getQnaPage(qnaSearchDto, pageable);
    }

    public QnaResponseDto getQnaById(Long qnaId) {
        // QnaRepository를 사용하여 게시글을 가져옴
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);

        return new QnaResponseDto(qna);
    }
}
