package name.cnsler.restvote.web.meal;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Meal;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.MealRepository;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminMealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMealController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/meals";
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId,
                                                   @Valid @RequestBody Meal meal) {
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        Meal created = mealRepository.save(meal);
        log.info("{} created", created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int restaurantId, @PathVariable int id) {
        Meal meal = getMealBelongRestaurant(id, restaurantId);
        log.info("get {}", meal);
        return meal;
    }

    @PutMapping(value = "/{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId,
                       @PathVariable int id,
                       @Valid @RequestBody Meal meal) {
        Meal updatableMeal = getMealBelongRestaurant(id, restaurantId);
        log.info("updatableMeal={}", updatableMeal);
        meal.setId(id);
        Restaurant restaurant = updatableMeal.getRestaurant();
        meal.setRestaurant(restaurant);
        log.info("update {}", meal);
        mealRepository.save(meal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        Meal meal = getMealBelongRestaurant(id, restaurantId);
        log.info("delete meal {}", meal);
        mealRepository.delete(meal);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int restaurantId) {
        Restaurant restaurant = restaurantRepository.getExists(restaurantId, Restaurant.class);
        List<Meal> meals = mealRepository.findAllByRestaurantId(restaurantId,
                Sort.by("mealDate").descending().and(Sort.by("name")));
        log.info("get all meals {} for restaurant id={}", meals, restaurantId);
        return meals;
    }

    private Meal getMealBelongRestaurant(int id, int restaurantId) {
        return mealRepository.getByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException(
                        "Meal with id=" + id + " for restaurant id=" + restaurantId + " not found"));
    }
}