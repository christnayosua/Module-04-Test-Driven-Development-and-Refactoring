package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private Model model;

    @InjectMocks
    private CarController carController;

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
    void testCreateCarPage() {
        String viewName = carController.createCarPage(model);
        assertEquals("createCar", viewName);
        verify(model).addAttribute(eq("car"), any(Car.class));
    }

    @Test
    void testCreateCarPost() {
        String viewName = carController.createCarPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).create(car);
    }

    @Test
    void testCarListPage() {
        List<Car> carList = Arrays.asList(car, new Car());
        when(carService.findAll()).thenReturn(carList);

        String viewName = carController.carListPage(model);

        assertEquals("carList", viewName);
        verify(model).addAttribute("cars", carList);
        verify(carService).findAll();
    }

    @Test
    void testEditCarPageWhenCarExists() {
        when(carService.findById("test-id")).thenReturn(car);

        String viewName = carController.editCarPage("test-id", model);

        assertEquals("editCar", viewName);
        verify(model).addAttribute("car", car);
        verify(carService).findById("test-id");
    }

    @Test
    void testEditCarPageWhenCarNotFound() {
        when(carService.findById("unknown")).thenReturn(null);

        String viewName = carController.editCarPage("unknown", model);

        assertEquals("redirect:/car/list", viewName);
        verify(model, never()).addAttribute(anyString(), any());
        verify(carService).findById("unknown");
    }

    @Test
    void testEditCarPost() {
        String viewName = carController.editCarPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).update(car.getCarId(), car);
    }

    @Test
    void testDeleteCar() {
        String carId = "test-id";
        String viewName = carController.deleteCar(carId);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).deleteCarById(carId);
    }
}