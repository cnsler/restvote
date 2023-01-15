package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Meal;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends BaseRepository<Meal> {

    List<Meal> findAllByRestaurantId(int restaurantId, Sort sort);

    List<Meal> findAllByRestaurantIdAndMealDate(int restaurantId, LocalDate mealDate);

    Optional<Meal> getByIdAndRestaurantId(int id, int restaurantId);
}