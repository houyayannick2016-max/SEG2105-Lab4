package com.example.lab4;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void testDefaultConstructor() {
        Product product = new Product();

        assertNull(product.getProductId());
        assertNull(product.getProductName());
        assertEquals(0.0, product.getProductPrice(), 0.001);
    }

    @Test
    public void testParameterizedConstructor() {
        Product product = new Product("P001", "Laptop", 999.99);

        assertEquals("P001", product.getProductId());
        assertEquals("Laptop", product.getProductName());
        assertEquals(999.99, product.getProductPrice(), 0.001);
    }

    @Test
    public void testSetters() {
        Product product = new Product();

        product.setProductId("P002");
        product.setProductName("Keyboard");
        product.setProductPrice(49.99);

        assertEquals("P002", product.getProductId());
        assertEquals("Keyboard", product.getProductName());
        assertEquals(49.99, product.getProductPrice(), 0.001);
    }

    @Test
    public void testToString() {
        Product product = new Product("P003", "Mouse", 25.50);

        assertEquals("Mouse - $25.5", product.toString());
    }
}