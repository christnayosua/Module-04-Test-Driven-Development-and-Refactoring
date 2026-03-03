package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryImplTest {

    private CarRepositoryImpl carRepository;

    private Car createTestCar(String id, String name, String color, int quantity) {
        Car car = new Car();
        car.setCarId(id);
        car.setCarName(name);
        car.setCarColor(color);
        car.setCarQuantity(quantity);
        return car;
    }

    @BeforeEach
    void setUp() {
        carRepository = new CarRepositoryImpl();
    }

    @Test
    void testCreateCarWithExistingId() {
        Car car = createTestCar("car-123", "Tesla", "Red", 10);
        Car created = carRepository.create(car);
        assertNotNull(created);
        assertEquals("car-123", created.getCarId());
        assertEquals("Tesla", created.getCarName());
        assertEquals("Red", created.getCarColor());
        assertEquals(10, created.getCarQuantity());
        assertTrue(carRepository.findAll().hasNext());
    }

    @Test
    void testCreateCarWithoutId() {
        Car car = new Car();
        car.setCarName("BMW");
        car.setCarColor("Blue");
        car.setCarQuantity(5);
        Car created = carRepository.create(car);
        assertNotNull(created);
        assertNotNull(created.getCarId());
        assertEquals("BMW", created.getCarName());
        assertEquals("Blue", created.getCarColor());
        assertEquals(5, created.getCarQuantity());
    }

    @Test
    void testCreateCarWithEmptyId() {
        Car car = new Car();
        car.setCarId("");
        car.setCarName("Audi");
        car.setCarColor("Black");
        car.setCarQuantity(3);
        Car created = carRepository.create(car);
        assertNotNull(created);
        assertNotNull(created.getCarId());
        assertFalse(created.getCarId().isEmpty());
        assertEquals("Audi", created.getCarName());
        assertEquals("Black", created.getCarColor());
        assertEquals(3, created.getCarQuantity());
    }

    @Test
    void testFindAllWhenEmpty() {
        Iterator<Car> iterator = carRepository.findAll();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllWithMultipleCars() {
        carRepository.create(createTestCar("id1", "Car1", "Red", 1));
        carRepository.create(createTestCar("id2", "Car2", "Blue", 2));
        carRepository.create(createTestCar("id3", "Car3", "Green", 3));
        Iterator<Car> iterator = carRepository.findAll();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testFindByIdWhenExists() {
        carRepository.create(createTestCar("find-id", "Toyota", "Silver", 7));
        Car found = carRepository.findById("find-id");
        assertNotNull(found);
        assertEquals("find-id", found.getCarId());
        assertEquals("Toyota", found.getCarName());
        assertEquals("Silver", found.getCarColor());
        assertEquals(7, found.getCarQuantity());
    }

    @Test
    void testFindByIdWhenNotExists() {
        assertNull(carRepository.findById("nonexistent"));
    }

    @Test
    void testFindByIdWithNullId() {
        assertNull(carRepository.findById(null));
    }

    @Test
    void testUpdateCarSuccess() {
        carRepository.create(createTestCar("update-id", "Honda", "White", 2));
        Car updated = createTestCar("update-id", "Honda Civic", "Black", 5);
        Car result = carRepository.update("update-id", updated);
        assertNotNull(result);
        assertEquals("Honda Civic", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
        Car retrieved = carRepository.findById("update-id");
        assertEquals("Honda Civic", retrieved.getCarName());
        assertEquals("Black", retrieved.getCarColor());
        assertEquals(5, retrieved.getCarQuantity());
    }

    @Test
    void testUpdateCarNotFound() {
        Car updated = createTestCar("nonexistent", "Mazda", "Yellow", 3);
        assertNull(carRepository.update("nonexistent", updated));
    }

    @Test
    void testUpdateCarWithNullId() {
        Car updated = createTestCar(null, "NullID", "Purple", 1);
        assertNull(carRepository.update(null, updated));
    }

    @Test
    void testDeleteCarSuccess() {
        carRepository.create(createTestCar("delete-id", "Ford", "Gray", 4));
        assertNotNull(carRepository.findById("delete-id"));
        carRepository.delete("delete-id");
        assertNull(carRepository.findById("delete-id"));
    }

    @Test
    void testDeleteCarNotFound() {
        carRepository.delete("nonexistent");
    }

    @Test
    void testDeleteCarWithNullId() {
        carRepository.delete(null);
    }

    @Test
    void testMultipleOperations() {
        carRepository.create(createTestCar("c1", "Car1", "Red", 10));
        carRepository.create(createTestCar("c2", "Car2", "Blue", 20));
        carRepository.create(createTestCar("c3", "Car3", "Green", 30));

        assertEquals(3, countIterator(carRepository.findAll()));

        Car updatedC2 = createTestCar("c2", "Car2 Updated", "Cyan", 25);
        carRepository.update("c2", updatedC2);
        Car foundC2 = carRepository.findById("c2");
        assertEquals("Car2 Updated", foundC2.getCarName());
        assertEquals("Cyan", foundC2.getCarColor());
        assertEquals(25, foundC2.getCarQuantity());

        carRepository.delete("c1");
        assertNull(carRepository.findById("c1"));
        assertNotNull(carRepository.findById("c2"));
        assertNotNull(carRepository.findById("c3"));
        assertEquals(2, countIterator(carRepository.findAll()));
    }

    @Test
    void testCreateCarWithNullNameAndColor() {
        Car car = new Car();
        car.setCarId("null-fields");
        car.setCarName(null);
        car.setCarColor(null);
        car.setCarQuantity(0);
        Car created = carRepository.create(car);
        assertNotNull(created);
        assertNull(created.getCarName());
        assertNull(created.getCarColor());
        assertEquals(0, created.getCarQuantity());
    }

    private int countIterator(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}