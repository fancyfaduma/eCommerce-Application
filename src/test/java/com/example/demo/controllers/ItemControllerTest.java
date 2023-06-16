package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_getItems(){
        Item item = new Item();
        item.setName("theItem");
        List<Item>  items = new ArrayList<>();
        items.add(item);
        when(itemRepo.findByName("theItem")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("theItem");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> list = response.getBody();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals("theItem", list.get(0).getName());


        when(itemRepo.findById(1L)).thenReturn(Optional.of(new Item()));
        ResponseEntity<Item> response2 = itemController.getItemById(1L);
        assertNotNull(response2);
        assertEquals(200, response2.getStatusCodeValue());

        when(itemRepo.findByName("item")).thenReturn(Collections.emptyList());
        response = itemController.getItemsByName("item");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        when(itemRepo.findByName("item")).thenReturn(null);
        response = itemController.getItemsByName("item");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        when(itemRepo.findAll()).thenReturn(null);
        response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }



}
