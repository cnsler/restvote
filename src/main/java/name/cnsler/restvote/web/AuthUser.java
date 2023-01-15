package name.cnsler.restvote.web;

import lombok.Getter;
import name.cnsler.restvote.model.User;
import org.springframework.lang.NonNull;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "email=" + super.getUsername() +
                ", roles=" + super.getAuthorities() +
                '}';
    }
}