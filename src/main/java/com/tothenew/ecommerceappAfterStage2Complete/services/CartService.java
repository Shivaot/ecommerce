package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CartDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.Cart;
import com.tothenew.ecommerceappAfterStage2Complete.entities.product.ProductVariation;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CartRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.ProductVariationRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UserEmailFromToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private ProductVariationRepo productVariationRepo;
    @Autowired
    private UserEmailFromToken userEmailFromToken;
    Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addToCart(CartDTO cartDTO, HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Optional<ProductVariation> productVariation = productVariationRepo.findById(cartDTO.getProductVariationId());
        if (!productVariation.isPresent()) {
            throw new ResourceNotFoundException(cartDTO.getProductVariationId() + " does not exist");
        }
        if (cartRepo.findByVariationIdAndCustomerId(cartDTO.getProductVariationId(),customer.getId()).isPresent()) {
            throw new FieldAlreadyExistException(cartDTO.getProductVariationId() + " already in cart");
        }
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProductVariation(productVariation.get());
        cart.setQuantity(1);
        cart.setWishListItem(cartDTO.getWishListItem());
        customerRepo.save(customer);
        cartRepo.save(cart);
        logger.info(cartDTO.getProductVariationId() + " successfully added to cart");
    }

    public List<?> getCartItems(HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        List<Cart> cart = cartRepo.findByCustomer(customer);
        if (cart.isEmpty()) {
            return new ArrayList<>(Collections.singleton("cart is empty"));
        }
        return cart;
    }

    public void incrementCartItem(Long id,HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Optional<Cart> cart = cartRepo.findByVariationIdAndCustomerId(id,customer.getId());
        if (!cart.isPresent()) {
            throw new ResourceNotFoundException("Product " + id + " doesnt exist cart");
        }
        cart.get().setQuantity(cart.get().getQuantity() + 1);
        cartRepo.save(cart.get());
    }

    @Transactional
    public void decrementCartItem(Long id,HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Optional<Cart> cart = cartRepo.findByVariationIdAndCustomerId(id,customer.getId());
        if (!cart.isPresent()) {
            throw new ResourceNotFoundException("Product " + id + " doesnt exist cart");
        }
        if (cart.get().getQuantity().compareTo(1) == 0) {
            removeCartItem(id,request);
            return;
        }
        cart.get().setQuantity(cart.get().getQuantity() - 1);
        cartRepo.save(cart.get());
    }

    @Transactional
    public void removeCartItem(Long id,HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        Optional<Cart> cart = cartRepo.findByVariationIdAndCustomerId(id,customer.getId());
        if (!cart.isPresent()) {
            throw new ResourceNotFoundException("Product " + id + " doesnt exist in cart");
        }
        cartRepo.deleteByCartId(cart.get().getId());
    }
}
