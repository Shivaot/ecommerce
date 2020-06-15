package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CartDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/addItem")
    public ResponseEntity<?> addProductToCart(@RequestBody CartDTO cartDTO,HttpServletRequest request) {
        logger.info("Inside add Product To Cart");
        cartService.addToCart(cartDTO,request);
        return new ResponseEntity<>(new ResponseDTO("Product Added to Cart",new Date()), HttpStatus.CREATED);
    }

    @GetMapping
    public List<?> getCartItems(HttpServletRequest request) {
        logger.info("Inside get cart items");
        return cartService.getCartItems(request);
    }

    @PatchMapping("/incrementItem")
    public ResponseEntity<?> increaseCartItem(@RequestBody Map<String,Long> payLoad, HttpServletRequest request) {
        logger.info("Inside increment cart item");
        cartService.incrementCartItem(payLoad.get("productId"),request);
        return new ResponseEntity<>(new ResponseDTO(" Product Quantity Increased", new Date()),HttpStatus.OK);
    }

    @PatchMapping("/decrementItem")
    public ResponseEntity<?> decreaseCartItem(@RequestBody Map<String,Long> payLoad, HttpServletRequest request) {
        logger.info("Inside decrement cart item");
        cartService.decrementCartItem(payLoad.get("productId"),request);
        return new ResponseEntity<>(new ResponseDTO(" Product Quantity Decreased", new Date()),HttpStatus.OK);
    }

    @DeleteMapping("/removeItem")
    public ResponseEntity<?> removeCartItem(@RequestBody Map<String,Long> payLoad, HttpServletRequest request) {
        logger.info("Inside remove cart item");
        cartService.removeCartItem(payLoad.get("productId"),request);
        return new ResponseEntity<>(new ResponseDTO("Product Removed from cart",new Date()),HttpStatus.OK);
    }
}
