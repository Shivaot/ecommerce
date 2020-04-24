package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.category.CategoryMetadataField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryMetadataFieldRepo extends CrudRepository<CategoryMetadataField,Long> {
    CategoryMetadataField findByName(String name);
    List<CategoryMetadataField> findAll(Pageable pageable);
}
