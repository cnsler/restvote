package name.cnsler.restvote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_date")})
@Getter
@Setter
public class Vote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "User must not be null")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Restaurant must not be null")
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false)
    @NotNull(message = "Date must not be null")
    private LocalDate date;

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", restaurantId=" + restaurant.getId() +
                ", voteDate=" + date +
                '}';
    }
}