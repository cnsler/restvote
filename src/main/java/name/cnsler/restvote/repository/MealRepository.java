package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Meal;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends BaseRepository<Meal> {

    Optional<Meal> getByIdAndRestaurantId(int id, int restaurantId);
}