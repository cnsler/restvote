package name.cnsler.restvote.web;

import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.AppException;
import name.cnsler.restvote.util.validation.ValidationUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = ex.updateAndGetBody(this.messageSource, LocaleContextHolder.getLocale());
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), getErrorMessage(error));
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(error.getField(), getErrorMessage(error));
        }
        body.setProperty("invalid_params", invalidParams);
        body.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

//   https://howtodoinjava.com/spring-mvc/spring-problemdetail-errorresponse/#5-adding-problemdetail-to-custom-exceptions
    @ExceptionHandler(AppException.class)
    public ProblemDetail appException(AppException ex, WebRequest request) {
        log.error("ApplicationException: {}", ex.getMessage());
        return createProblemDetail(ex, ex.getStatusCode(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflict(DataIntegrityViolationException ex, WebRequest request) {
        String message = ValidationUtil.getRootCause(ex).getMessage();
        log.error("DataIntegrityViolationException: {}", message);
        String customMessage = null;
        if (message.toLowerCase().contains("restaurant(title")) {
            customMessage = "Restaurant with this title already existed";
        }
        if (message.toLowerCase().contains("foreign key(restaurant_id)")) {
            customMessage = "Invalid restaurant id";
        }
        if (message.toLowerCase().contains("vote_unique_user_date")) {
            customMessage = "One vote per day";
        }
        if (message.toLowerCase().contains("meal_unique_restaurant_date_name")) {
            customMessage = "One unique meal name on day for restaurant";
        }
        return createProblemDetail(ex, HttpStatus.CONFLICT, request, customMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail constraint(ConstraintViolationException ex, WebRequest request) {
        String message = ValidationUtil.getRootCause(ex).getMessage();
        log.error("ConstraintViolationException: {}", message);
        return createProblemDetail(ex, HttpStatus.UNPROCESSABLE_ENTITY, request, null);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ProblemDetail empty(EmptyResultDataAccessException ex, WebRequest request) {
        String message = ValidationUtil.getRootCause(ex).getMessage();
        log.error("EmptyResultDataAccessException: {}", message);
        return createProblemDetail(ex, HttpStatus.UNPROCESSABLE_ENTITY, request, null);
    }

    private ProblemDetail createProblemDetail(Exception ex, HttpStatusCode statusCode, WebRequest request, @Nullable String customMessage) {
        String message = customMessage != null ? customMessage : ex.getMessage();
        log.error("Error message: {}", message);
        return createProblemDetail(ex, statusCode, message, null, null, request);
    }

    private String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(
                error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale());
    }
}
