package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import com.tothenew.ecommerceappAfterStage2Complete.entities.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerAllProductByCategoryDTO {
    private List<Product> products;
    private String image;
    private List<String> images;
    List<HashMap<Long, String>> prices = new ArrayList<>();


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<HashMap<Long, String>> getPrices() {
        return prices;
    }

    public void setPrices(List<HashMap<Long, String>> prices) {
        this.prices = prices;
    }
}
