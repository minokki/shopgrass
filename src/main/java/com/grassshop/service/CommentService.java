package com.grassshop.service;

import com.grassshop.dto.CommentRequestDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Comment;
import com.grassshop.entity.Qna;
import com.grassshop.repository.AccountRepository;
import com.grassshop.repository.CommentRepository;
import com.grassshop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final QnaRepository qnaRepository;

    //댓글작성
    @Transactional
    public Long commentSave(String nickname, Long id, CommentRequestDto dto) {
        Account account = accountRepository.findByNickname(nickname);

        Qna qna = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));
        dto.setAccount(account);
        dto.setQna(qna);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }

    @Transactional
    public void commentUpdate(Long qnaId, Long commentId, CommentRequestDto dto) {

        Comment comment = commentRepository.findByQnaIdAndId(qnaId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        // CommentRequestDto에서 업데이트할 내용을 가져와서 엔티티에 반영
        comment.update(dto.getComment());
    }

    @Transactional
    public void commentDelete(Long qnaId, Long commentId) {
        Comment comment = commentRepository.findByQnaIdAndId(qnaId, commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));
        commentRepository.delete(comment);
    }
}
