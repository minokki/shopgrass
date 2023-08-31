package com.grassshop.repository;

import com.grassshop.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> , QuerydslPredicateExecutor<Qna>, QnaRepositoryCustom {
}
