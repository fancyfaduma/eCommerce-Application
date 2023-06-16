package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_negative_path(){
        ModifyCartRequest request  = new ModifyCartRequest();
        request.setUsername("fatima");
        request.setItemId(3L);
        request.setQuantity(6);

        when(userRepo.findByUsername("fatima")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        User user = new User();
        user.setCart(new Cart());


        when(userRepo.findByUsername("fatima")).thenReturn(user);
        when(itemRepo.findById(3L)).thenReturn(Optional.empty());

        response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }


    @Test
    public void add_to_cart_happy_path(){
        ModifyCartRequest request  = new ModifyCartRequest();
        request.setUsername("fatima");
        request.setItemId(3L);
        request.setQuantity(3);

        User user = new User();
        user.setCart(new Cart());

        Item item = new Item();
        item.setPrice(BigDecimal.valueOf(3));

        when(userRepo.findByUsername("fatima")).thenReturn(user);
        when(itemRepo.findById(request.getItemId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);

    }




}
