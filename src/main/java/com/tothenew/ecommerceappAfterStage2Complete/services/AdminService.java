package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminCustomerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminSellerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    SellerRepo sellerRepo;

    public List<AdminCustomerDTO> getCustomers(String page, String size, String sortBy, Optional<String> email) {
        List<AdminCustomerDTO> adminCustomerDTOS = new ArrayList<>();
        if (email.isPresent()) {
            Customer customer = customerRepo.findByEmail(email.get());
            if (customer == null) {
                throw new FieldAlreadyExistException("email not found");
            }
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
        });
        return adminCustomerDTOS;
    }

    public List<AdminSellerDTO> getSellers(String page, String size, String sortBy, Optional<String> email) {
        List<AdminSellerDTO> adminSellerDTOS = new ArrayList<>();
        if (email.isPresent()) {
            Seller seller = sellerRepo.findByEmail(email.get());
            if (seller == null) {
                throw new FieldAlreadyExistException("email not found");
            }
            AdminSellerDTO adminSellerDTO = new AdminSellerDTO();
            adminSellerDTO.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
            adminSellerDTO.setEmail(email.get());
            adminSellerDTO.setActive(seller.isActive());
            adminSellerDTO.setId(seller.getId());
            adminSellerDTO.setCompanyAddress(seller.getAddresses());
            adminSellerDTO.setCompanyName(seller.getCompanyName());
            adminSellerDTO.setCompanyContact(seller.getCompanyContact());
            adminSellerDTOS.add(adminSellerDTO);
            return adminSellerDTOS;
        }
        List<Seller> sellers = sellerRepo.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sortBy)));
        sellers.forEach(seller -> {
            AdminSellerDTO adminSellerDTO = new AdminSellerDTO();
            adminSellerDTO.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
            adminSellerDTO.setEmail(seller.getEmail());
            adminSellerDTO.setActive(seller.isActive());
            adminSellerDTO.setId(seller.getId());
            adminSellerDTO.setCompanyAddress(seller.getAddresses());
            adminSellerDTO.setCompanyName(seller.getCompanyName());
            adminSellerDTO.setCompanyContact(seller.getCompanyContact());
            adminSellerDTOS.add(adminSellerDTO);
        });
        return adminSellerDTOS;
    }
}
