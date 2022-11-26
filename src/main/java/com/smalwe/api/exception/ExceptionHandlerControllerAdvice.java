package com.smalwe.api.exception;

import com.smalwe.api.bean.ApiError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RestTemplateException.class, NullPointerException.class})
    protected ResponseEntity<ApiError> handleRestTemplateException (RestTemplateException ex, WebRequest request) {
        logger.info("In handle RestTemplateException");
        ApiError errors = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultHandleException(Exception ex, WebRequest request) {
        logger.error("Exception resulting in internal server error", ex.getMessage());
        ApiError errors = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        return new ResponseEntity<Object>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UnProcessableEntityException.class})
    public ResponseEntity<Object> handleUnprocessedInputException(UnProcessableEntityException ex, WebRequest request) {
        ApiError errors = new ApiError(HttpStatus.BAD_REQUEST, "400 Bad Request");
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiError errors = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(errors, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable (HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError errors = new ApiError(HttpStatus.BAD_REQUEST, "Invalid input");
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }



    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info(ex.getMessage());

        List<String> errList = new ArrayList<>();
        ApiError errors = new ApiError(HttpStatus.BAD_REQUEST, "Invalid request payload object", errList);
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            logger.info("Message:" + message);
            errList.add(message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<Object> badRequestHandler(Exception ex, WebRequest request) {
        ApiError errors = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<Object>(errors, HttpStatus.UNAUTHORIZED);
    }
}