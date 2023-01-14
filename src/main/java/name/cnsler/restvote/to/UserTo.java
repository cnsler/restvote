package name.cnsler.restvote.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import name.cnsler.restvote.HasIdAndEmail;
import name.cnsler.restvote.util.validation.NoHtml;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail {
    @Email(message = "E-mail must have correct format")
    // https://stackoverflow.com/questions/17480809
    @NoHtml(message = "E-mail must not contain html content")
    @NotBlank(message = "E-mail must not be empty")
    @Size(max = 128)
    String email;

    @NotBlank(message = "Password must not be empty")
    @Size(max = 256)
    // https://stackoverflow.com/a/12505165/548473
    String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}