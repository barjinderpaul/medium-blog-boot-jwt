package com.blog.medium.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(
                404,
                e.getMessage(),
                "Go to localhost:8080/swagger-ui.html#/ for the documentation",
                HttpStatus.NOT_FOUND
        );
        log.error("Not found error",e);
        return new ResponseEntity<>(errorMessage,httpStatus);
    }

    @ExceptionHandler(value = {InvalidArgumentException.class})
    public ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage(
            400,
            e.getMessage(),
            "Go to localhost:8080/swagger-ui.html#/ for the documentation",
            HttpStatus.BAD_REQUEST
        );
        log.error("Invalid Arguments passed",e);
        return new ResponseEntity<>(errorMessage,httpStatus);
    }

    @ExceptionHandler(value={UnauthorizedAccessException.class})
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException e){
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ErrorMessage errorMessage = new ErrorMessage(
          403,
          e.getMessage(),
        "Go to localhost:8080/swagger-ui.html#/ for the documentation",
        HttpStatus.FORBIDDEN
        );
        log.error("Unauthorized access ",e);
        return new ResponseEntity<>(errorMessage,httpStatus);
    }

    @ExceptionHandler(value = {GenericException.class})
    public ResponseEntity<Object> handleGenericException(GenericException e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage(
                500,
                e.getMessage(),
                "Go to localhost:8080/swagger-ui.html#/ for the documentation",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error("Some unknown exception occured, please check stack trace",e);
        return new ResponseEntity<>(errorMessage,httpStatus);
    }
}
