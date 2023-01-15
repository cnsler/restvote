package name.cnsler.restvote.web.meal;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Meal;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.MealRepository;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = AdminMealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMealController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/meals";
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Meal meal) {
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        //TODO catch Exception
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
        Optional<Meal> optionalMeal = mealRepository.getByIdAndRestaurantId(id, restaurantId);
        Meal meal = optionalMeal.orElseThrow(
                () -> new IllegalRequestDataException(
                        "Meal with id=" + id + " for restaurant id=" + restaurantId + " not found"));
        log.info("get {}", meal);
        return meal;
    }

    @PutMapping(value = "/{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Meal meal) {
        Optional<Meal> optionalMeal = mealRepository.getByIdAndRestaurantId(id, restaurantId);
        Meal updatableMeal = optionalMeal.orElseThrow(
                () -> new IllegalRequestDataException(
                        "Meal with id=" + id + " for restaurant id=" + restaurantId + " not found"));
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
        //TODO check restaurant id exists? Or change path /api/meals/{id}
        //TODO check meal belong to restaurant?
        log.info("delete meal with id={}", id);
        mealRepository.deleteExisted(id);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int restaurantId) {
        //TODO optimize SQL query: without Restaurant title (r1_0.title=?)
        Meal mealProbe = new Meal();
        mealProbe.setRestaurant(restaurantRepository.getExisted(restaurantId));
        List<Meal> meals = mealRepository.findAll(Example.of(mealProbe));
        log.info("getAll meals {} for restaurant id={}", meals, restaurantId);
        return meals;
    }
}