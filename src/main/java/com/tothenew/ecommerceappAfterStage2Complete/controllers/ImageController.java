package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.services.FileStorageService;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ImageController {

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/uploadImage")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = fileStorageService.storeFile(file,request);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/home/shiva/Documents/javaPrograms/afterStage2/afterStage2/src/main/resources/static/users/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
}
