package com.tothenew.ecommerceappAfterStage2Complete.dtos;

public class CartDTO {

    private Long productVariationId;
    private boolean isWishListItem;

    public Long getProductVariationId() {
        return productVariationId;
    }

    public void setProductVariationId(Long productVariationId) {
        this.productVariationId = productVariationId;
    }

    public boolean getWishListItem() {
        return isWishListItem;
    }

    public void setWishListItem(boolean wishListItem) {
        isWishListItem = wishListItem;
    }
}
