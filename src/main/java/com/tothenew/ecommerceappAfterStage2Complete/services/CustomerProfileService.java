package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AddressDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.CustomerProfileDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Address;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ContactInvalidException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidPasswordException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.AddressRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UserEmailFromToken;
import com.tothenew.ecommerceappAfterStage2Complete.utils.PasswordValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerProfileService {

    @Autowired
    UserEmailFromToken userEmailFromToken;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EmailSender emailSender;
    @Autowired
    AddressRepo addressRepo;

    public Customer viewProfile(HttpServletRequest request) {
        String customerEmail = userEmailFromToken.getUserEmail(request);
        Customer customer = customerRepo.findByEmail(customerEmail);
        return customer;
    }

    public String updateCustomer(CustomerProfileDTO customerProfileDTO, HttpServletRequest request) {
        Set<String> imageExtensionsAllowed = new HashSet<>();
        imageExtensionsAllowed.add("jpg");
        imageExtensionsAllowed.add("jpeg");
        imageExtensionsAllowed.add("png");
        imageExtensionsAllowed.add("bmp");
        if (!(customerProfileDTO.getContact() == null) && (customerProfileDTO.getContact().length()!=10)) {
            throw new ContactInvalidException("invalid contact");
        }
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        try {
            if (!(customerProfileDTO.getFirstName()  == null)){
                customer.setFirstName(customerProfileDTO.getFirstName());
            }
            if (!(customerProfileDTO.getLastName() == null)){
                customer.setLastName(customerProfileDTO.getLastName());
            }
            if (!(customerProfileDTO.getContact() == null)) {
                customer.setContact(customerProfileDTO.getContact());
            }
            if (!(customerProfileDTO.getImage() == null)) {
                File fi;
                File[] matchingFiles = new File[2];
                try
                {
                    try {
                        File f = new File("/home/shiva/software/afterStage2/src/main/resources/static/users");
                        matchingFiles = f.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(customer.getId().toString());
                            }
                        });
                        fi = new File(matchingFiles[0].toString());
                        Path fileToDeletePath = Paths.get(String.valueOf(fi));
                        Files.delete(fileToDeletePath);
                    } catch(Exception ex) {}

                    String parts[] = customerProfileDTO.getImage().split(",");
                    String imageName = parts[0];
                    String fileExtensionArr[] = imageName.split("/");
                    String fileExtension[] = fileExtensionArr[1].split(";");
                    if (!imageExtensionsAllowed.contains(fileExtension[0])) {
                        return fileExtension + " image format not allowed";
                    }
                    String imageString = parts[1];

                    BufferedImage image = null;
                    byte[] imageByte;

                    BASE64Decoder decoder = new BASE64Decoder();
                    imageByte = decoder.decodeBuffer(imageString);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                    image = ImageIO.read(bis);
                    bis.close();
                    String path = "/home/shiva/software/afterStage2/src/main/resources/static/users/" + customer.getId();

                    File outputFile = new File(path+"."+fileExtension[0]);
                    ImageIO.write(image, fileExtension[0], outputFile);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }
        customerRepo.save(customer);
        return "Success";
    }

    public boolean updatePassword(String pass,String cPass,HttpServletRequest request) {
        if (!pass.contentEquals(cPass)) {
            throw new InvalidPasswordException("Password and confirm password does not match");
        }
        if (!PasswordValidator.isValidPassword(pass)) {
            throw new InvalidPasswordException("password format invalid");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        customer.setPassword(passwordEncoder.encode(pass));
        customerRepo.save(customer);
        emailSender.sendEmail("PASSWORD CHANGED","Your password changed",customer.getEmail());
        return true;
    }

    public boolean newAddress(AddressDTO addressDTO,HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Address address = modelMapper.map(addressDTO,Address.class);
        Set<Address> addresses = customer.getAddresses();
        addresses.add(address);
        customer.setAddresses(addresses);
        addresses.forEach(a -> {
            Address addressSave = a;
            addressSave.setUser(customer);
        });
        customerRepo.save(customer);
        return true;
    }

    public Set<AddressDTO> viewAddress(HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Set<Address> addresses = customer.getAddresses();
        Set<AddressDTO> addressDTOs = new HashSet<>();
        addresses.forEach(a -> {
            AddressDTO addressDTO = modelMapper.map(a,AddressDTO.class);
            addressDTOs.add(addressDTO);
        });
        return addressDTOs;
    }

    @Transactional
    public boolean deleteAddress(Long id) {
        Optional<Address> address = addressRepo.findById(id);
        if (!address.isPresent()) {
            throw  new ResourceNotFoundException("no address fount with id " + id);
        }
        addressRepo.deleteById(id);
        return true;
    }

    public boolean updateAddress(Long id,AddressDTO addressDTO,HttpServletRequest request) {
        Optional<Address> address = addressRepo.findById(id);
        if (!address.isPresent()) {
            throw  new ResourceNotFoundException("no address fount with id " + id);
        }
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Set<Address> addresses = customer.getAddresses();
        addresses.forEach(a->{
            if (a.getId().compareTo(address.get().getId()) == 0) {
                a.setCity(addressDTO.getCity());
                a.setCountry(addressDTO.getCountry());
                a.setLabel(addressDTO.getLabel());
                a.setState(addressDTO.getState());
                a.setZipCode(addressDTO.getZipCode());
                a.setAddress(addressDTO.getAddress());
            }
        });
        customerRepo.save(customer);
        return true;
    }

}
