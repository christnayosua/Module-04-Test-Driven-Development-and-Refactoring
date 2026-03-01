package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class CarRepository {

    private final List<Car> carData = new ArrayList<>();

    // CREATE
    public Car create(Car car) {
        if (car.getCarId() == null) {
            UUID uuid = UUID.randomUUID();
            car.setCarId(uuid.toString());
        }

        carData.add(car);
        return car;
    }

    // READ ALL
    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    // READ BY ID
    public Car findById(String id) {
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    // UPDATE
    public Car update(String id, Car updatedCar) {
        for (int i = 0; i < carData.size(); i++) {
            Car car = carData.get(i);

            if (car.getCarId().equals(id)) {
                // update data
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());

                return car;
            }
        }
        return null; // jika tidak ditemukan
    }

    // DELETE
    public void delete(String id) {
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}