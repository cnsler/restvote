package name.cnsler.restvote.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant with id=" + id + " not found"));
        log.info("get {}", restaurant);
        return restaurant;
    }

    //TODO get restaurant with meals on date or current date:
    // /api/restaurant/{id}/with-meals{?date=yyyy-MM-dd}
    // RestaurantTo{id(?), title, [{name, price}, ...]}

    @GetMapping
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        log.info("get all {}", restaurants);
        return restaurants;
    }

    //TODO get all with meals?
}