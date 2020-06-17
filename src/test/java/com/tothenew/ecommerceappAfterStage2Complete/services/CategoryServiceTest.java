package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void shouldThrowFieldAlreadyExistExceptionWhenAddingRootCategory() {
        String categoryName = "Men";
        Optional<Long> parentId = Optional.of(8L);
        Executable executable = () -> categoryService.addCategory(categoryName,parentId);
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    // will not always pass
    @Test
    void shouldCreateAChildCategory() {
        String categoryName = "Suits";
        Optional<Long> parentId = Optional.of(7L);
        String result = categoryService.addCategory(categoryName,parentId);
        assertThat(result).contains("Success");
    }

    // will not always pass
    @Test
    void shouldCreateARootCategory() {
        String categoryName = "Men";
        Optional<Long> parentId = Optional.empty();
        String result = categoryService.addCategory(categoryName,parentId);
        assertThat(result).contains("Success");
    }
    @Test
    void shouldThrowResourceNotFoundExceptionWhenPassedInvalidId() {
        Long categoryId = 700L;
        Executable executable = () -> categoryService.deleteCategory(categoryId);
        assertThrows(ResourceNotFoundException.class,executable);
    }

    @Test
    void shouldReturnParentCategory() {
        Long categoryId = 7L;
        String result = categoryService.deleteCategory(categoryId);
        assertThat(result).contains("parent category");
    }

    // will not always pass
    @Test
    void shouldDeleteACategory() {
        Long categoryId = 14L;
        String result = categoryService.deleteCategory(categoryId);
        assertThat(result).contains("Success");
    }

}