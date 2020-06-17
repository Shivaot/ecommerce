package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.category.CategoryMetadataField;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MetadataServiceTest {

    @Autowired
    private MetadataService metadataService;

    @Test
    void shouldThrowFieldAlreadyExistException() {
        String fieldName = "Size";
        Executable executable = () -> metadataService.addMetadata(fieldName);
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    @Test
    void shouldAddMetadata() {
        String fieldName = "Ram";
        String result = metadataService.addMetadata(fieldName);
        assertThat(result).contains("Success");
    }

    // will not always pass
    @Test
    void shouldReturnAllMetadata() {
        Optional<String> query = Optional.empty();
        List<CategoryMetadataField> categoryMetadataFields = metadataService.viewMetadata("0","10","id","asc",query);
        assertTrue(categoryMetadataFields.size() > 2);
    }
}