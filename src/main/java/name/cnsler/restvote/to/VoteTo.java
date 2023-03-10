package name.cnsler.restvote.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    @NotNull(message = "Restaurant id must not be null")
    @Positive(message = "Restaurant id must be greater than 0")
    Integer restaurantId;

    public VoteTo(Integer id, Integer restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }
}