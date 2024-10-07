package com.orion.service.customer;

import com.github.f4b6a3.ulid.UlidCreator;
import com.orion.entity.Rental;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.customer.CustomerDto;
import com.orion.entity.Customer;
import com.orion.entity.Tenant;
import com.orion.repository.CustomerRepository;
import com.orion.repository.TenantRepository;
import com.orion.service.BaseService;
import com.orion.service.user.TenantService;
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
public class CustomerService extends BaseService {
    private final CustomerRepository repository;
    private final TenantService tenantService;

    public ResponseObject createCustomer(CustomerDto customerDto) {
        String methodName = "createCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setLicenseNumber(UlidCreator.getUlid().toString());
        customer.setTenant(tenant);

        responseObject.setData(this.save(customer));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getCustomer(Long customerId) {
        String methodName = "getCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<CustomerDto> customer = repository.findCustomerByIdFromDto(customerId, ConfigSystem.getTenant().getId());
        isPresent(customer);

        List<Rental> rentals = repository.findCustomerRentals(customerId);
        if(!rentals.isEmpty()){
            customer.get().setRentals(rentals);
        }

        responseObject.setData(customer.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject getAll(String currentEmail){
        String methodName = "getAll";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        List<CustomerDto> customers = repository.findAllCustomersFromDto(ConfigSystem.getTenant().getId(),currentEmail);
        responseObject.setData(Optional.of(customers).orElse(Collections.emptyList()));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateCustomer(Long customerId, CustomerDto customerDto) {
        String methodName = "updateCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Customer customerToUpdate = findById(customerId);
        customerToUpdate.setName(customerDto.getName());
        customerToUpdate.setEmail(customerDto.getEmail());
        customerToUpdate.setPhoneNumber(customerDto.getPhoneNumber());

        responseObject.setData(this.save(customerToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteCustomer(Long customerId) {
        String methodName = "deleteCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Customer customer = findById(customerId);
        customer.setDeletedAt(LocalDateTime.now());
        this.save(customer);

        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public Customer findById(Long customerId){
        Optional<Customer> customer = repository.findCustomerByIdAndTenantId(customerId, ConfigSystem.getTenant().getId());
        isPresent(customer);
        return customer.get();
    }

    public Customer save(Customer customer){
        try {
            return this.repository.save(customer);
        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
        }
        return null;
    }
    public List<Long> findCustomerIdsFromAgencies(List<String> agencyEmails){
        List<Long> customerIds = repository.findCustomerIdsFromAgencies(agencyEmails);

        if(customerIds == null || customerIds.isEmpty()){
            return Collections.emptyList();
        }
        return customerIds;
    }
}
