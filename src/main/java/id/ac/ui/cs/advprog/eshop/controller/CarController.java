package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/create")
    public String createCarPage(Model model) {
        model.addAttribute("car", new Car());
        return "createCar";
    }

    @PostMapping("/create")
    public String createCarPost(@ModelAttribute Car car) {
        carService.create(car);
        return "redirect:/car/list";
    }

    @GetMapping("/list")
    public String carListPage(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "carList";
    }

    @GetMapping("/edit/{carId}")
    public String editCarPage(@PathVariable String carId, Model model) {
        Car car = carService.findById(carId);
        if (car == null) {
            return "redirect:/car/list";
        }
        model.addAttribute("car", car);
        return "editCar";
    }

    @PostMapping("/edit")
    public String editCarPost(@ModelAttribute Car car) {
        carService.update(car.getCarId(), car);
        return "redirect:/car/list";
    }

    @PostMapping("/delete")
    public String deleteCar(@RequestParam("carId") String carId) {
        carService.deleteCarById(carId);
        return "redirect:/car/list";
    }
}