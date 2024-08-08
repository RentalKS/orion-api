package com.orion.service;

import com.orion.entity.Rental;
import com.orion.generics.ResponseObject;
import com.orion.config.tenant.TenantContext;
import com.orion.dto.customer.CustomerDto;
import com.orion.entity.Customer;
import com.orion.entity.Tenant;
import com.orion.repository.CustomerRepository;
import com.orion.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService extends BaseService {

    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;

    public ResponseObject createCustomer(CustomerDto customerDto) {
        String methodName = "createCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setLicenseNumber(customerDto.getLicenseNumber());
        customer.setTenant(tenant.get());

        responseObject.setData(customerRepository.save(customer));
        responseObject.prepareHttpStatus(HttpStatus.CREATED);

        return responseObject;
    }

    public ResponseObject getCustomer(Long customerId) {
        String methodName = "getCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<CustomerDto> customer = customerRepository.findCustomerByIdFromDto(customerId, tenant.get().getId());
        isPresent(customer);

        List<Rental> rentals = customerRepository.findCustomerRentals(customerId);
        if(!rentals.isEmpty()){
            customer.get().setRentals(rentals);
        }

        responseObject.setData(customer.get());
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject updateCustomer(Long customerId, CustomerDto customerDto) {
        String methodName = "updateCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Customer> customer = customerRepository.findById(customerDto.getId());
        isPresent(customer);

        Customer customerToUpdate = customer.get();
        customerToUpdate.setName(customerDto.getName());
        customerToUpdate.setEmail(customerDto.getEmail());
        customerToUpdate.setPhoneNumber(customerDto.getPhoneNumber());
        customerToUpdate.setLicenseNumber(customerDto.getLicenseNumber());
        customerToUpdate.setTenant(tenant.get());

        responseObject.setData(customerRepository.save(customerToUpdate));
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject deleteCustomer(Long customerId) {
        String methodName = "deleteCustomer";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Optional<Customer> customer = customerRepository.findById(customerId);
        isPresent(customer);

        customer.get().setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer.get());
        responseObject.setData(true);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
}
