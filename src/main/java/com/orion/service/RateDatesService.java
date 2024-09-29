package com.orion.service;

import com.orion.infrastructure.tenant.TenantContext;
import com.orion.entity.Tenant;
import com.orion.generics.ResponseObject;
import com.orion.dto.rates.RatesDto;
import com.orion.entity.RateDates;
import com.orion.repository.RateDatesRepository;
import com.orion.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class RateDatesService extends BaseService {

    private final RateDatesRepository rateDatesRepository;
    private final TenantRepository tenantRepository;

    public ResponseObject createRateDates(RatesDto rateDatesDto) {
        String methodName = "createRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> optionalTenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(optionalTenant);

        RateDates rateDates = new RateDates();
        rateDates.setName(rateDatesDto.getName());
        rateDates.setDailyRate(rateDatesDto.getDailyRate());
        rateDates.setWeeklyRate(rateDatesDto.getWeeklyRate());
        rateDates.setMonthlyRate(rateDatesDto.getMonthlyRate());
        rateDates.setTenant(optionalTenant.get());


        responseObject.setData(rateDatesRepository.save(rateDates));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);
        return responseObject;
    }

    public ResponseObject getRateDates(Long RateDatesId) {
        String methodName = "getRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<RateDates> RateDates = rateDatesRepository.findById(RateDatesId);
        if (RateDates.isPresent()) {
            responseObject.setData(RateDates.get());
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } else {
            responseObject.setData("RateDates not found");
            responseObject.prepareHttpStatus(HttpStatus.NOT_FOUND);
        }

        return responseObject;
    }

    public ResponseObject updateRateDates(Long rateDateId, RatesDto RateDatesDto) {
        String methodName = "updateRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<RateDates> RateDatesOptional = rateDatesRepository.findById(rateDateId);
        if (RateDatesOptional.isPresent()) {
            RateDates RateDates = RateDatesOptional.get();
            RateDates.setName(RateDatesDto.getName());
            RateDates.setDailyRate(RateDatesDto.getDailyRate());
            RateDates.setWeeklyRate(RateDatesDto.getWeeklyRate());
            RateDates.setMonthlyRate(RateDatesDto.getMonthlyRate());

            responseObject.setData(rateDatesRepository.save(RateDates));
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } else {
            responseObject.setData("RateDates not found");
            responseObject.prepareHttpStatus(HttpStatus.NOT_FOUND);
        }

        return responseObject;
    }

    public ResponseObject deleteRateDates(Long RateDatesId) {
        String methodName = "deleteRateDates";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<RateDates> RateDates = rateDatesRepository.findById(RateDatesId);
        if (RateDates.isPresent()) {
            rateDatesRepository.delete(RateDates.get());
            responseObject.setData(true);
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } else {
            responseObject.setData("RateDates not found");
            responseObject.prepareHttpStatus(HttpStatus.NOT_FOUND);
        }

        return responseObject;
    }

    public ResponseObject getAllRateDatess() {
        String methodName = "getAllRateDatess";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        List<RateDates> RateDatess = rateDatesRepository.findAll();
        responseObject.setData(RateDatess);
        responseObject.prepareHttpStatus(HttpStatus.OK);

        return responseObject;
    }
}