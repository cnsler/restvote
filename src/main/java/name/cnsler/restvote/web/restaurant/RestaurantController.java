package name.cnsler.restvote.web.restaurant;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.model.Meal;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.MealRepository;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        Restaurant restaurant = restaurantRepository.getExists(id, Restaurant.class);
        log.info("get {}", restaurant);
        return restaurant;
    }

    //TODO get restaurant with meals on date or current date:
    // /api/restaurant/{id}/with-meals{?date=yyyy-MM-dd}
    // RestaurantTo{id(?), title, [{name, price}, ...]}
    @GetMapping("/{id}/meals-on-date")
    public List<Meal> getMealsOnDate(@PathVariable int id, @Nullable LocalDate date) {
        LocalDate mealDate = Objects.requireNonNullElse(date, LocalDate.now());
        Restaurant restaurant = restaurantRepository.getExists(id, Restaurant.class);
        List<Meal> meals = mealRepository.findAllByRestaurantIdAndDate(restaurant.id(), mealDate);
        log.info("get all meals {} for restaurant id={} on date={}", meals, id, mealDate);
        return meals;
    }

    @GetMapping
    @Cacheable(value = "restaurant")
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        log.info("get all {}", restaurants);
        return restaurants;
    }
}