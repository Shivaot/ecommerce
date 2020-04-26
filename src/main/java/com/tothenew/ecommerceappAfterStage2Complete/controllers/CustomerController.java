package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AddressDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.CustomerProfileDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.category.Category;
import com.tothenew.ecommerceappAfterStage2Complete.services.CategoryService;
import com.tothenew.ecommerceappAfterStage2Complete.services.CustomerProfileService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/customer/profile")
public class CustomerController {

    @Autowired
    CustomerProfileService customerProfileService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public CustomerProfileDTO viewProfile(HttpServletRequest request) throws IOException {
        CustomerProfileDTO customerProfileDTO = modelMapper.map(customerProfileService.viewProfile(request),CustomerProfileDTO.class);
        File f = new File("/home/shiva/Documents/javaPrograms/afterStage2/afterStage2/src/main/resources/static/users");
        File[] matchingFiles = new File[2];
        try {
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith(customerProfileDTO.getId().toString());
                }
            });
        }
        catch (Exception ex) {}
        if (matchingFiles.length>0) {
            File file = new File(matchingFiles[0].toString());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encodedFile = new String(Base64.encodeBase64(fileContent), "UTF-8");
            customerProfileDTO.setImage(encodedFile);
        }

        return customerProfileDTO;
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO> updateProfile(@RequestBody CustomerProfileDTO customerProfileDTO, HttpServletRequest request) {
        String getMessage = customerProfileService.updateCustomer(customerProfileDTO,request);
        if ("Success".contentEquals(getMessage)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody Map<String,String> body,HttpServletRequest request) {
        if (customerProfileService.updatePassword(body.get("pass"),body.get("cPass"),request)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/addresses")
    public Set<AddressDTO> viewAddresses(HttpServletRequest request ) {
        return customerProfileService.viewAddress(request);
    }

    @PostMapping("/address")
    public ResponseEntity<ResponseDTO> newAddress(@RequestBody AddressDTO addressDTO, HttpServletRequest request) {
        if (customerProfileService.newAddress(addressDTO,request)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<ResponseDTO> deleteAddress(@PathVariable Long id) {
        if (customerProfileService.deleteAddress(id)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<ResponseDTO> updateAddress(@PathVariable Long id,@RequestBody AddressDTO addressDTO,HttpServletRequest request) {
        if (customerProfileService.updateAddress(id,addressDTO,request)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/categories")
    public List<Category> viewLeafCategories(@RequestParam Optional<Long> categoryId) {
        return categoryService.viewCategoriesSameParent(categoryId);
    }

    @GetMapping("/filterCategories/{categoryId}")
    public List<?> filterCategory(@PathVariable Long categoryId) {
        return categoryService.filterCategory(categoryId);
    }
}
