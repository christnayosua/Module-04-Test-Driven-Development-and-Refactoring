package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("test-id");
        car.setCarName("Test Car");
        car.setCarColor("Red");
        car.setCarQuantity(10);
    }

    @Test
    void testCreate() {
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
        assertEquals("Test Car", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testCreateWithNullId() {
        Car carWithoutId = new Car();
        carWithoutId.setCarName("No ID");
        when(carRepository.create(carWithoutId)).thenReturn(carWithoutId);

        Car result = carService.create(carWithoutId);

        assertNotNull(result);
        assertNull(result.getCarId());
        assertEquals("No ID", result.getCarName());
        verify(carRepository).create(carWithoutId);
    }

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Car car2 = new Car();
        car2.setCarId("test-id-2");
        carList.add(car2);

        Iterator<Car> iterator = carList.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test-id", result.get(0).getCarId());
        assertEquals("test-id-2", result.get(1).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWhenEmpty() {
        Iterator<Car> emptyIterator = new ArrayList<Car>().iterator();
        when(carRepository.findAll()).thenReturn(emptyIterator);

        List<Car> result = carService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository).findAll();
    }

    @Test
    void testFindByIdExists() {
        when(carRepository.findById("test-id")).thenReturn(car);

        Car result = carService.findById("test-id");

        assertNotNull(result);
        assertEquals("test-id", result.getCarId());
        assertEquals("Test Car", result.getCarName());
        verify(carRepository, times(1)).findById("test-id");
    }

    @Test
    void testFindByIdNotExists() {
        when(carRepository.findById("non-existent")).thenReturn(null);

        Car result = carService.findById("non-existent");

        assertNull(result);
        verify(carRepository).findById("non-existent");
    }

    @Test
    void testFindByIdWithNull() {
        when(carRepository.findById(null)).thenReturn(null);

        Car result = carService.findById(null);

        assertNull(result);
        verify(carRepository).findById(null);
    }

    @Test
    void testUpdate() {
        Car updatedCar = new Car();
        updatedCar.setCarId("test-id");
        updatedCar.setCarName("Updated Car");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(5);

        when(carRepository.update("test-id", updatedCar)).thenReturn(updatedCar);

        carService.update("test-id", updatedCar);

        verify(carRepository, times(1)).update("test-id", updatedCar);
    }

    @Test
    void testUpdateWithNonExistentId() {
        Car updatedCar = new Car();
        updatedCar.setCarId("non-existent");
        updatedCar.setCarName("Updated");
        when(carRepository.update("non-existent", updatedCar)).thenReturn(null);

        carService.update("non-existent", updatedCar);

        verify(carRepository).update("non-existent", updatedCar);
        // No exception expected, just verify call
    }

    @Test
    void testDeleteCarById() {
        doNothing().when(carRepository).delete("test-id");

        carService.deleteCarById("test-id");

        verify(carRepository, times(1)).delete("test-id");
    }

    @Test
    void testDeleteCarByIdWithNull() {
        doNothing().when(carRepository).delete(null);

        carService.deleteCarById(null);

        verify(carRepository).delete(null);
    }
}