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
    @NotBlank
    @NoHtml
    private String title;
}