package com.grassshop.repository;

import com.grassshop.entity.Ntc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NtcRepository extends JpaRepository<Ntc,Long>, QuerydslPredicateExecutor<Ntc>, NtcRepositoryCustom{
}
