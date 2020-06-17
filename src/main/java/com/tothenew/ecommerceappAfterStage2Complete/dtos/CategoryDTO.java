package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import com.tothenew.ecommerceappAfterStage2Complete.entities.category.Category;

import java.util.HashMap;
import java.util.Set;

public class CategoryDTO{
    private Category category;
    private Set<HashMap<String,String>> filedValuesSet;
    private Set<Category> childCategory;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<HashMap<String, String>> getFiledValuesSet() {
        return filedValuesSet;
    }

    public void setFiledValuesSet(Set<HashMap<String, String>> filedValuesSet) {
        this.filedValuesSet = filedValuesSet;
    }

    public Set<Category> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(Set<Category> childCategory) {
        this.childCategory = childCategory;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "category=" + category +
                ", filedValuesSet=" + filedValuesSet +
                ", childCategory=" + childCategory +
                '}';
    }
}
