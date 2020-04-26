package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UserEmailFromToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Autowired
    UserEmailFromToken userEmailFromToken;
    @Autowired
    UserRepo userRepo;

    public String storeFile(MultipartFile file, HttpServletRequest request) {
        String email = userEmailFromToken.getUserEmail(request);
        User user = userRepo.findByEmail(email);

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new ResourceNotFoundException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get("/home/shiva/Documents/javaPrograms/afterStage2/afterStage2/src/main/resources/static/users/" + user.getId());
            System.out.println(targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Could not store file " + fileName + ". Please try again!");
        }
    }
}
