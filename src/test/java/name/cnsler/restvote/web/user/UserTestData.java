package name.cnsler.restvote.web.user;

import name.cnsler.restvote.model.Role;
import name.cnsler.restvote.model.User;
import name.cnsler.restvote.util.JsonUtil;
import name.cnsler.restvote.web.MatcherFactory;

import java.util.Collections;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;
    public static final int NOT_FOUND = 100;
    public static final String ADMIN_MAIL = "a@a";
    public static final String USER_MAIL = "u@u";

    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "adminPass", Role.ADMIN, Role.USER);
    public static final User user = new User(USER_ID, "User", USER_MAIL, "userPass", Role.USER);

    public static User getNew() {
        return new User(null, "NewUser", "n@u", "newPass", Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdUser", USER_MAIL, "newPass", Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "userPass", passw);
    }
}
