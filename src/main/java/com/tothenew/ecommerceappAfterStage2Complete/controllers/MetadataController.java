package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.entities.category.CategoryMetadataField;
import com.tothenew.ecommerceappAfterStage2Complete.services.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    @Autowired
    MetadataService metadataService;

    @PostMapping("/add")
    public String addMetadata(@RequestParam String fieldName, HttpServletResponse response) {
        String getMessage = metadataService.addMetadata(fieldName);
        if (getMessage.contains("Success")) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @GetMapping("/view")
    public List<CategoryMetadataField> viewMetadata(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "50") String size, @RequestParam(defaultValue = "id") String SortBy, @RequestParam(defaultValue = "ASC") String order, @RequestParam Optional<String> query) {
        return metadataService.viewMetadata(page,size,SortBy,order,query);
    }
}
