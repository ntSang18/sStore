package com.pproject.sStore.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final Integer PAGE_SIZE = 8;

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

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public List<Order> getRecentOrders() {
        List<Order> orders = getAdminOrders();
        orders.sort((Order o1, Order o2) -> {
            return o1.getCreateAt().compareTo(o2.getCreateAt());
        });
        if (orders.size() < 5) {
            return orders;
        }
        return orders.subList(0, 5);
    }

    public List<Order> getAdminOrders() {
        return orderRepository.getAdminOrders();
    }

    public Page<Order> getShipOrders1(int pageNum, String search, int sort) {
        if (sort == 1) {
            return orderRepository.getShipperOrders1(search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("createAt").descending()));
        } else if (sort == 2) {
            return orderRepository.getShipperOrders1(search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("createAt").ascending()));
        } else if (sort == 3) {
            return orderRepository.getShipperOrders1(search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("totalPrice").descending()));
        } else if (sort == 4) {
            return orderRepository.getShipperOrders1(search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("totalPrice").ascending()));
        } else {
            return orderRepository.getShipperOrders1(search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE));
        }
    }

    public Page<Order> getShipOrders2(Long shipperId, int pageNum, String search, int sort) {
        if (sort == 1) {
            return orderRepository.getShipperOrders2(shipperId, search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("createAt").descending()));
        } else if (sort == 2) {
            return orderRepository.getShipperOrders2(shipperId, search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("createAt").ascending()));
        } else if (sort == 3) {
            return orderRepository.getShipperOrders2(shipperId, search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("totalPrice").descending()));
        } else if (sort == 4) {
            return orderRepository.getShipperOrders2(shipperId, search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE,
                    Sort.by("totalPrice").ascending()));
        } else {
            return orderRepository.getShipperOrders2(shipperId, search, PageRequest.of(
                    pageNum - 1,
                    PAGE_SIZE));
        }
    }

    public List<Order> getUserOrders(Long uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalStateException("User by id " + uid + " does not exits"));
        return user.getOrders();
    }

    public int countNewOrder() {
        return orderRepository.countNewOrder();
    }

    public Double totalSalesByMonth() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        double totalSales = 0L;
        for (Order o : getAllOrder()) {
            if (o.getStatus() > 1 && o.getCreateAt().getMonthValue() == currentMonth) {
                totalSales += o.getTotalPrice();
            }
        }
        return totalSales;
    }

    public void newOrder(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User by id " + userId + " does not exits"));
            Order order = orderRepository.save(new Order(LocalDate.now(), 1, user));
            Long totalPrice = 0L;
            int totalQuantity = 0;
            for (int i = 0; i < user.getCart().getItems().size(); i++) {
                ProductItem item = user.getCart().getItems().get(i);
                item.setOrder(order);
                item.setStatus(1);
                totalQuantity += item.getQuantity();
                totalPrice += item.getQuantity() * item.getProduct().getPrice();
                user.getCart().delItems(item.getId());
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new IllegalStateException("Product not found"));
                product.inSold(Long.valueOf(item.getQuantity()));
                productItemRepository.save(item);
                cartRepository.save(user.getCart());
                productRepository.save(product);
            }
            order.setTotalQuantity(totalQuantity);
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getListAdminOrder(String search, Long filterMode) {
        List<Order> orders = new ArrayList<Order>();
        if (search.trim() == "") {
            orders = getAdminOrders();
        } else {
            for (Order o : getAdminOrders()) {
                if (o.getUser().getUserName().toLowerCase().contains(search.toLowerCase())) {
                    orders.add(o);
                }
            }
        }
        if (filterMode == 0) {
            orders.sort((Order o1, Order o2) -> {
                return o1.getId().compareTo(o2.getId());
            });
        } else if (filterMode == 1) {
            orders.sort((Order o1, Order o2) -> {
                return o1.getTotalPrice().compareTo(o2.getTotalPrice());
            });
        } else if (filterMode == 2) {
            orders.sort((Order o1, Order o2) -> {
                if (o1.getTotalPrice().compareTo(o2.getTotalPrice()) < 0) {
                    return 1;
                } else {
                    return -1;
                }
            });
        } else if (filterMode == 3) {
            orders.sort((Order o1, Order o2) -> {
                return o1.getCreateAt().compareTo(o2.getCreateAt());
            });
        } else if (filterMode == 4) {
            orders.sort((Order o1, Order o2) -> {
                if (o1.getCreateAt().compareTo(o2.getCreateAt()) < 0) {
                    return 1;
                } else {
                    return -1;
                }
            });
        }
        return orders;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order by id " + id + " does not exit"));
    }

    public void confirmOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(2);
        for (ProductItem item : order.getItems()) {
            item.setStatus(2);
            productItemRepository.save(item);
        }
        orderRepository.save(order);
    }

    public void adminDelOrder(Long oid) {
        Order order = getOrderById(oid);
        order.setAdminVisible(-1);
        orderRepository.save(order);
    }

    public void shipperTakeOrder(User shipper, Long oid) {
        Order order = getOrderById(oid);
        order.setShipper(shipper);
        order.setStatus(3);
        orderRepository.save(order);
    }

    public void shipperDeliveredOrder(User shipper, Long oid) {
        Order order = getOrderById(oid);
        if (order.getShipper().getId() == shipper.getId()) {
            order.setStatus(4);
            orderRepository.save(order);
        }
    }
}
