package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void testDefaultConstructor() {
        Car car = new Car();
        assertNull(car.getCarId());
        assertNull(car.getCarName());
        assertNull(car.getCarColor());
        assertEquals(0, car.getCarQuantity());
    }

    @Test
    void testSettersAndGetters() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Tesla");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        assertEquals("car-123", car.getCarId());
        assertEquals("Tesla", car.getCarName());
        assertEquals("Red", car.getCarColor());
        assertEquals(5, car.getCarQuantity());
    }
}