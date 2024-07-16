package com.example.apidemo.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String PRODUCT_NOT_FOUND = "Product cannot be found";
    public static final String REVIEW_NOT_FOUND = "Review cannot be found";
    public static final String PRODUCT_NOT_FOUND_CODE = "productNotFound";
    public static final String REVIEW_NOT_FOUND_CODE = "reviewNotFound";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String OVER_QUANTITY = "Over quantity";
    public static final String OVER_QUANTITY_CODE = "overQuantity";
    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CART_NOT_FOUND_CODE = "cartNotFoundCode";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found";
    public static final String CART_ITEM_NOT_FOUND_CODE = "cartItemNotFoundCode";

    public static final String ITEM_ALREADY_EXIST_IN_CART = "Item already exist in cart";
    public static final String ITEM_ALREADY_EXIST_IN_CART_CODE = "itemAlreadyExistInCart";

    public static final String ITEM_ALREADY_EXIST_IN_WISHLIST = "Item already exist in wishlist";
    public static final String ITEM_ALREADY_EXIST_IN_WISHLIST_CODE = "itemAlreadyExistInWishlist";
}
