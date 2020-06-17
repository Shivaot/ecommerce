package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class OrderProductDTO implements Serializable {

    private Integer quantity;
    private BigDecimal price;
    private Long productVariationId;
    private Map<String,Object> metadata;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductVariationId() {
        return productVariationId;
    }

    public void setProductVariationId(Long productVariationId) {
        this.productVariationId = productVariationId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "OrderProductDTO{" +
                "quantity=" + quantity +
                ", price=" + price +
                ", productVariationId=" + productVariationId +
                ", metadata=" + metadata +
                '}';
    }
}
