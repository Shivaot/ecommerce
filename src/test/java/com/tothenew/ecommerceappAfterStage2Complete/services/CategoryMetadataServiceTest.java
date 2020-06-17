package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CategoryMetadataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class CategoryMetadataServiceTest {

    @Autowired
    private CategoryMetadataService categoryMetadataService;

    @Test
    void shouldThrowResourceNotFoundException() {
        CategoryMetadataDTO categoryMetadataDTO = new CategoryMetadataDTO();
        categoryMetadataDTO.setCategoryId(12000L);
        HashMap<String, HashSet<String>> fieldValues = new HashMap<>();
        HashSet<String> values = new HashSet<>();
        values.add("12gb");values.add("8gb");
        fieldValues.put("609",values);
        categoryMetadataDTO.setFiledIdValues(fieldValues);
    }

    @Test
    void shouldAddCategoryMetadata() {
        CategoryMetadataDTO categoryMetadataDTO = new CategoryMetadataDTO();
        categoryMetadataDTO.setCategoryId(12L);
        HashMap<String, HashSet<String>> fieldValues = new HashMap<>();
        HashSet<String> values = new HashSet<>();
        values.add("12gb");values.add("8gb");
        fieldValues.put("609",values);
        categoryMetadataDTO.setFiledIdValues(fieldValues);
        String result = categoryMetadataService.addCategoryMetadata(categoryMetadataDTO);
        assertThat(result).contains("Success");
    }
}