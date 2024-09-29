package com.orion.service.RentalService;

import com.orion.infrastructure.tenant.TenantContext;
import com.orion.infrastructure.tenant.TenantContextService;
import com.orion.entity.RateDates;
import com.orion.repository.RateDatesRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateDateService extends BaseService {
    private final RateDatesRepository rateDatesRepository;

    public RateDates findRateDateById(Long rateDateId){
        Optional<RateDates> optionalRateDates = rateDatesRepository.findRateById(rateDateId, TenantContext.getCurrentTenant().getId());
        isPresent(optionalRateDates);
        return optionalRateDates.get();
    }

}
