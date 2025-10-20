package com.orion.service.rental;

import com.orion.entity.Customer;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.entity.Tenant;
import com.orion.generics.ResponseObject;
import com.orion.dto.rates.RatesDto;
import com.orion.entity.RateDates;
import com.orion.repository.RateDatesRepository;
import com.orion.service.BaseService;
import com.orion.service.user.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class RateDatesService extends BaseService {

    private final RateDatesRepository repository;
    private final TenantService tenantService;

    public ResponseObject createRateDates(@Valid RatesDto rateDatesDto) {
        String methodName = "createRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Tenant tenant = tenantService.findById();

        RateDates rateDates = new RateDates();
        rateDates.setName(rateDatesDto.getName());
        rateDates.setDailyRate(rateDatesDto.getDailyRate());
        rateDates.setWeeklyRate(rateDatesDto.getWeeklyRate());
        rateDates.setMonthlyRate(rateDatesDto.getMonthlyRate());
        rateDates.setTenant(tenant);

        responseObject.setData(this.save(rateDates));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }

    public ResponseObject getRateDates(Long id,String currentEmail) {
        String methodName = "getRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<RatesDto> rateDates = repository.findByRateDates(id,currentEmail);
        isPresent(rateDates);
        responseObject.setData(rateDates);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject updateRateDates(Long id, RatesDto rateDatesDto) {
        String methodName = "updateRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        RateDates rateDates = findById(id);

        rateDates.setName(rateDatesDto.getName());
        rateDates.setDailyRate(rateDatesDto.getDailyRate());
        rateDates.setWeeklyRate(rateDatesDto.getWeeklyRate());
        rateDates.setMonthlyRate(rateDatesDto.getMonthlyRate());

        responseObject.setData(this.save(rateDates));
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }

    public ResponseObject deleteRateDates(Long id) {
        String methodName = "deleteRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        RateDates rateDates = findById(id);
        rateDates.setDeletedAt(LocalDateTime.now());
        this.save(rateDates);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        responseObject.setData(true);

        return responseObject;
    }

    public ResponseObject getAllRateDates(String currentEmail) {
        String methodName = "getAllRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        List<RatesDto> rateDatesList = repository.findAllRateDates(currentEmail);
        responseObject.setData(Optional.of(rateDatesList).orElse(Collections.emptyList()));
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
    public RateDates findById(Long rateDateId){
        Optional<RateDates> optionalRateDates = repository.findRateById(rateDateId, ConfigSystem.getTenant().getId());
        isPresent(optionalRateDates);
        return optionalRateDates.get();
    }

    public RateDates save(RateDates rateDates){
        try {
            return this.repository.save(rateDates);
        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
        }
        return null;
    }
}