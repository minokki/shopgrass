package com.grassshop.ApiController;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.CommentRequestDto;
import com.grassshop.entity.Account;
import com.grassshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/qna/{qnaId}/comments")
    public ResponseEntity commentSave(@PathVariable Long qnaId, @RequestBody CommentRequestDto dto,
                                      @CurrentUser Account account) {

        return ResponseEntity.ok(commentService.commentSave(account.getNickname(), qnaId, dto));
    }

    @PutMapping({"/qna/{qnaId}/comments/{commentId}"})
    public ResponseEntity<Long> update(@PathVariable Long qnaId, @PathVariable Long commentId, @RequestBody CommentRequestDto dto) {
        commentService.commentUpdate(qnaId, commentId, dto);
        return ResponseEntity.ok(commentId);
    }

    @DeleteMapping({"/qna/{qnaId}/comments/{commentId}"})
    public ResponseEntity<Long> delete(@PathVariable Long qnaId, @PathVariable Long commentId) {
        commentService.commentDelete(qnaId, commentId);
        return ResponseEntity.ok(commentId);
    }
}
