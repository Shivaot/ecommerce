package com.tothenew.ecommerceappAfterStage2Complete.entities.users;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.Cart;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderTable;
import com.tothenew.ecommerceappAfterStage2Complete.entities.product.ProductReview;

import javax.persistence.*;
import java.util.Set;


@Entity
@PrimaryKeyJoinColumn(name="user_id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends User {

    private String contact;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<ProductReview> productReviews;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<OrderTable> orderTables;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Cart cart;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(Set<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    public Set<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(Set<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
