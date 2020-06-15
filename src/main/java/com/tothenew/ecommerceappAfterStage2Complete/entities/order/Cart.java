package com.tothenew.ecommerceappAfterStage2Complete.entities.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tothenew.ecommerceappAfterStage2Complete.entities.product.ProductVariation;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;

import javax.persistence.*;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer quantity;
    private Boolean isWishListItem;

    @OneToOne
    @JoinColumn(name = "customer_user_id",updatable = true,insertable = true,unique = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_variation_id",updatable = true,insertable = true,unique = true)
    private ProductVariation productVariation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getWishListItem() {
        return isWishListItem;
    }

    public void setWishListItem(Boolean wishListItem) {
        isWishListItem = wishListItem;
    }

    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }
}
