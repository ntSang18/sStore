package com.pproject.sStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pproject.sStore.model.Product;
import com.pproject.sStore.model.ProductItem;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.CartRepository;
import com.pproject.sStore.repository.ProductItemRepository;
import com.pproject.sStore.repository.ProductRepository;
import com.pproject.sStore.repository.UserRepository;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ProductItemRepository productItemRepository;

    @Autowired
    public CartService(UserRepository userRepository, ProductRepository productRepository,
            CartRepository cartRepository, ProductItemRepository productItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.productItemRepository = productItemRepository;
    }

    public String addToCart(ProductItem productItem, Long userId, Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalStateException("Product by id " + productId + "does not exists"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User by id " + userId + "does not exists"));
            productItem.setProduct(product);
            // productItem.setCart(user.getCart());
            productItem.setStatus(0);
            user.getCart().addItems(productItem);
            cartRepository.save(user.getCart());
            return "Add product " + product.getProductName() + " to cart success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
