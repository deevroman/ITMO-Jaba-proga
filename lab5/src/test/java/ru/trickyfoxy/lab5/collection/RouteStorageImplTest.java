package ru.trickyfoxy.lab5.collection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteStorageImplTest {
    @Test
    void testConstructor() {
        new RouteStorageImpl();
    }

    @Test
    void testZeroSize() {
        RouteStorageImpl rs = new RouteStorageImpl();
        assertEquals(rs.size(), 0);
    }

    @Test
    void testClearEmptyStorage() {
        RouteStorageImpl rs = new RouteStorageImpl();
        rs.clear();
    }

}