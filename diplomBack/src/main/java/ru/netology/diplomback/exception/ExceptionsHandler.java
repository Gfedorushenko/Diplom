package ru.netology.diplomback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(DataError.class)
    public ResponseEntity<String> getDataError(DataError e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileError.class)
    public ResponseEntity<String> getFileError(FileError e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);//TODO 500
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<String> getUnauthorizedError(UnauthorizedError e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
