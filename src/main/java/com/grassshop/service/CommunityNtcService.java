package com.grassshop.service;

import com.grassshop.dto.NtcFormDto;
import com.grassshop.entity.CommunityNtc;
import com.grassshop.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityNtcService {
    private final CommunityRepository communityRepository;

    public Long saveCommunityNtc(NtcFormDto ntcFormDto) throws Exception{
        CommunityNtc ntc = ntcFormDto.createNtc();
        communityRepository.save(ntc);
        return ntc.getId();
    }

}
