package name.cnsler.restvote.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import name.cnsler.restvote.util.validation.NoHtml;

@Entity
@Getter
public class Restaurant extends BaseEntity {
    @Column(name = "title", nullable = false, unique = true)
    @NoHtml(message = "Restaurant title must not contain html content")
    @NotBlank(message = "Restaurant title must not be empty")
    private String title;

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}