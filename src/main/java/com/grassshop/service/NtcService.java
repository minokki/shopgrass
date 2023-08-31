package com.grassshop.service;

import com.grassshop.dto.NtcFormDto;
import com.grassshop.dto.NtcSearchDto;
import com.grassshop.entity.Ntc;
import com.grassshop.repository.NtcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

@Service
@Transactional
@RequiredArgsConstructor
public class NtcService {
    private final NtcRepository ntcRepository;

    public Long saveCommunityNtc(NtcFormDto ntcFormDto) {
        Ntc ntc = ntcFormDto.createNtc();
        ntcRepository.save(ntc);
        return ntc.getId();
    }

    @Transactional(readOnly = true)
    public NtcFormDto getCommunityNtc(Long ntcId) {
        Ntc ntc = ntcRepository.findById(ntcId).orElseThrow(EntityExistsException::new);

        NtcFormDto ntcFormDto = new NtcFormDto();
        ntcFormDto.setId(ntc.getId());
        ntcFormDto.setTitle(ntc.getTitle());
        ntcFormDto.setContent(ntc.getContent());
        ntcFormDto.setCreateBy(ntc.getCreateBy());
        ntcFormDto.setCreateDate(ntc.getRegTime());

        return ntcFormDto;
    }

    public Long updateCommunityNtc(NtcFormDto ntcFormDto) throws Exception {
        Ntc ntc = ntcRepository.findById(ntcFormDto.getId()).orElseThrow(EntityExistsException::new);
        ntc.updateNtc(ntcFormDto);
        return ntc.getId();
    }

    @Transactional(readOnly = true)
    public Page<Ntc> getNtcPage(NtcSearchDto ntcSearchDto, Pageable pageable) {
        return ntcRepository.getNtcPage(ntcSearchDto, pageable);
    }
}