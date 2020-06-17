package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CategoryDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.category.Category;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
        String categoryName = "Mobile";
        Optional<Long> parentId = Optional.of(12L);
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

    @Test
    void shouldThrowFieldAlreadyExistExceptionWhenUpdatingWithExistingName() {
        String categoryName = "Electronic";
        Long categoryId =  12L;
        Executable executable = () -> categoryService.updateCategory(categoryName,categoryId);
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    @Test
    void shouldUpdateCategoryName() {
        String categoryName = "Electronics";
        Long categoryId =  12L;
        String result = categoryService.updateCategory(categoryName,categoryId);
        assertThat(result).contains("Success");
    }

    @Test
    void shouldReturnCategoryDetails() {
        Long categoryId = 9L;
        CategoryDTO categoryDTO = categoryService.viewCategory(categoryId);
        assertThat(categoryDTO.getCategory().getName()).isEqualTo("Heals");
    }

    @Test
    void shouldReturnSingleCategoryWhenQueryIsPassed() {
        Optional<String> query = Optional.of("9");
        List<CategoryDTO> categoryDTOS = categoryService.viewCategories("0","10","id","asc",query);
        assertEquals(categoryDTOS.size(),1);
    }

    @Test
    void shouldReturnAllCategories() {
        Optional<String> query = Optional.empty();
        List<CategoryDTO> categoryDTOS = categoryService.viewCategories("0","10","id","asc",query);
        assertTrue(categoryDTOS.size() > 5);
    }

    @Test
    void shouldReturnLeafCategories() {
        List<CategoryDTO> categoryDTOS = categoryService.viewLeafCategories();
        final boolean[] flag = new boolean[1];
        categoryDTOS.forEach(categoryDTO -> {
            if (categoryDTO.getCategory().getName().compareTo("Heals") == 0) {
                   flag[0] = true;
            }
        });
        assertTrue(flag[0]);
    }

    @Test
    void shouldReturnCategoriesWithSameParent() {
        Optional<Long> categoryId = Optional.of(7L);
        List<Category> categories = categoryService.viewCategoriesSameParent(categoryId);
        assertThat(categories.size()).isEqualTo(2);
    }

}