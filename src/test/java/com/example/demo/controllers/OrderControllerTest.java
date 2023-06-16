package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void getOrders(){
        when(userRepo.findByUsername("fatima")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("fatima");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        when(userRepo.findByUsername("fatima")).thenReturn(new User());
        response = orderController.getOrdersForUser("fatima");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void submit_orders_happy_path(){
        Item item = new Item();
        item.setPrice(BigDecimal.valueOf(3));
        Cart cart = new Cart();
        cart.addItem(item);
        User user = new User();
        user.setUsername("fatima");
        user.setCart(cart);
        cart.setUser(user);

        when(userRepo.findByUsername("fatima")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("fatima");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
        assertEquals(BigDecimal.valueOf(3), userOrder.getTotal());
        assertEquals("fatima", userOrder.getUser().getUsername());

    }

    @Test
    public void submit_orders_negative_path(){
        when(userRepo.findByUsername("fatima")).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("fatima");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}

