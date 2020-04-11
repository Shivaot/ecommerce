package com.tothenew.ecommerceapp.entities.category;

import javax.persistence.*;

@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMedataFieldCompositeId categoryMedataFieldCompositeId;

    @ManyToOne
    @JoinColumn(name = "category_id",updatable = false,insertable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "category_metadata_field_id",updatable = false,insertable = false)
    private CategoryMetadataField categoryMetadataField;

    private String value;

    public CategoryMedataFieldCompositeId getCategoryMedataFieldCompositeId() {
        return categoryMedataFieldCompositeId;
    }

    public void setId(CategoryMedataFieldCompositeId categoryMedataFieldCompositeId) {
        this.categoryMedataFieldCompositeId = categoryMedataFieldCompositeId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String values) {
        this.value = values;
    }
}
