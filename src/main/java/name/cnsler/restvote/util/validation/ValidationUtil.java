package name.cnsler.restvote.util.validation;

import lombok.experimental.UtilityClass;
import name.cnsler.restvote.HasId;
import name.cnsler.restvote.error.IllegalRequestDataException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    //TODO don't use, contact the author!!
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static <T> void checkModification(int count, int id, Class<T> clazz) {
        if (count == 0) {
            throw new IllegalRequestDataException(clazz.getSimpleName() + " with id=" + id + " not found");
        }
    }

    public static <T> T checkExists(T obj, int id, Class<T> clazz) {
        if (obj == null) {
            throw new IllegalRequestDataException(clazz.getSimpleName() + " with id=" + id + " not found");
        }
        return obj;
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}