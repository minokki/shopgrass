package com.grassshop.repository;

import com.grassshop.constant.Example;
import com.grassshop.dto.*;
import com.grassshop.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardMainRepositoryCustomImpl implements BoardMainRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardMainRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QBoardMain.boardMain.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QBoardMain.boardMain.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QBoardMain.boardMain.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        List<BoardMain> content = queryFactory.selectFrom(QBoardMain.boardMain)
                .select(QBoardMain.boardMain)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery()))
                .orderBy(QBoardMain.boardMain.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QBoardMain.boardMain)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression boardMainTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoardMain.boardMain.title.like("%" + searchQuery + "%");
    }
    @Override
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QBoardMain boardMain = QBoardMain.boardMain;
        QBoardMainImg boardMainImg = QBoardMainImg.boardMainImg;

        Map<Long, MainBoardDto> boardDtoMap = new LinkedHashMap<>();

        List<Tuple> tuples = queryFactory
                .select(
                        boardMain.id,
                        boardMain.title,
                        boardMain.content,
                        boardMainImg.imgUrl,
                        boardMainImg.example
                )
                .from(boardMain)
                .leftJoin(boardMainImg).on(boardMain.id.eq(boardMainImg.boardMain.id))
                .where(boardMainTitleLike(boardSearchDto.getSearchQuery()))
                .orderBy(boardMain.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (Tuple tuple : tuples) {
            Long id = tuple.get(boardMain.id);
            String title = tuple.get(boardMain.title);
            String content = tuple.get(boardMain.content);
            String imgUrl = tuple.get(boardMainImg.imgUrl);
            Example example = tuple.get(boardMainImg.example);

            MainBoardDto boardDto = boardDtoMap.getOrDefault(id, new MainBoardDto());
            boardDto.setId(id);
            boardDto.setTitle(title);
            boardDto.setContent(content);

            if (Example.BEFORE.equals(example)) {
                boardDto.setImgUrlBefore(imgUrl);
            } else if (Example.AFTER.equals(example)) {
                boardDto.setImgUrlAfter(imgUrl);
            }

            boardDtoMap.put(id, boardDto);
        }

        List<MainBoardDto> content = new ArrayList<>(boardDtoMap.values());

        long total = queryFactory
                .select(Wildcard.count)
                .from(boardMain)
                .leftJoin(boardMainImg).on(boardMain.id.eq(boardMainImg.boardMain.id))
                .where(boardMainTitleLike(boardSearchDto.getSearchQuery()))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }


}
