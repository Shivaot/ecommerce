package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminCustomerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    CustomerRepo customerRepo;

    public List<AdminCustomerDTO> getCustomers(String page, String size, String sortBy, Optional<String> email) {
        List<AdminCustomerDTO> adminCustomerDTOS = new ArrayList<>();
        if (email.isPresent()) {
            Customer customer = customerRepo.findByEmail(email.get());
            AdminCustomerDTO adminCustomerDTO = new AdminCustomerDTO();
            adminCustomerDTO.setFullName(customer.getFirstName() + customer.getMiddleName() + " " +  customer.getLastName());
            adminCustomerDTO.setActive(customer.isActive());
            adminCustomerDTO.setEmail(customer.getEmail());
            adminCustomerDTO.setId(customer.getId());
            adminCustomerDTOS.add(adminCustomerDTO);
            return adminCustomerDTOS;
        }
        List<Customer> customers = customerRepo.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sortBy)));
        customers.forEach(customer -> {
            AdminCustomerDTO adminCustomerDTO = new AdminCustomerDTO();
            adminCustomerDTO.setFullName(customer.getFirstName() + customer.getMiddleName() + " " +  customer.getLastName());
            adminCustomerDTO.setActive(customer.isActive());
            adminCustomerDTO.setEmail(customer.getEmail());
            adminCustomerDTO.setId(customer.getId());
            adminCustomerDTOS.add(adminCustomerDTO);
            adminCustomerDTOS.add(adminCustomerDTO);
        });
        return adminCustomerDTOS;
    }
}
