package com.grassshop.repository;

import com.grassshop.dto.NtcSearchDto;
import com.grassshop.entity.Ntc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NtcRepositoryCustom {
    Page<Ntc> getNtcPage(NtcSearchDto ntcSearchDto, Pageable pageable);
}
