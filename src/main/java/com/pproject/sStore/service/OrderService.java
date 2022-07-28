package com.pproject.sStore.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pproject.sStore.model.Order;
import com.pproject.sStore.model.Product;
import com.pproject.sStore.model.ProductItem;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.CartRepository;
import com.pproject.sStore.repository.OrderRepository;
import com.pproject.sStore.repository.ProductItemRepository;
import com.pproject.sStore.repository.ProductRepository;
import com.pproject.sStore.repository.UserRepository;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;

    @Autowired
    public OrderService(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ProductItemRepository productItemRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productItemRepository = productItemRepository;
    }

    public void newOrder(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User by id " + userId + " does not exits"));
            Order order = orderRepository.save(new Order(LocalDate.now(), 1, user));
            for (ProductItem item : user.getCart().getItems()) {
            	item.setOrder(order);
            	productItemRepository.save(item);
                user.getCart().delItems(item.getId());
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new IllegalStateException("Product not found"));
                product.inSold(Long.valueOf(item.getQuantity()));
                cartRepository.save(user.getCart());
                productRepository.save(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
