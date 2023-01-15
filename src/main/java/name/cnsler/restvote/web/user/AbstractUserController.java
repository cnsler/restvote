package name.cnsler.restvote.web.user;

import name.cnsler.restvote.model.User;
import name.cnsler.restvote.repository.UserRepository;
import name.cnsler.restvote.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {

    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExists(id, User.class);
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}