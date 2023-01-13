package name.cnsler.restvote.web.meal;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.model.Meal;
import name.cnsler.restvote.repository.MealRepository;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = MealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class MealController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/meals";
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    //TODO return MealTo{id(?), name, price}?
    @GetMapping
    public List<Meal> getAllOnDate(@PathVariable int restaurantId, @Nullable LocalDate date) {
        Meal mealProbe = new Meal();
        mealProbe.setRestaurant(restaurantRepository.getExisted(restaurantId));
        LocalDate mealDate = Objects.requireNonNullElse(date, LocalDate.now());
        mealProbe.setMealDate(mealDate);
        List<Meal> meals = mealRepository.findAll(Example.of(mealProbe));
        log.info("getAll meals {} for restaurant id={} on date={}", meals, restaurantId, mealDate);
        return meals;
    }
}