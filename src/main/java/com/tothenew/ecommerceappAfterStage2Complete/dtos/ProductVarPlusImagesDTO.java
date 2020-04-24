package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import com.tothenew.ecommerceappAfterStage2Complete.entities.product.ProductVariation;

import java.util.List;
import java.util.Set;

public class ProductVarPlusImagesDTO {
    private Set<ProductVariation> productVariation;
    private List<String> images;

    public Set<ProductVariation> getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(Set<ProductVariation> productVariation) {
        this.productVariation = productVariation;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}