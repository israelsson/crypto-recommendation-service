package se.ai.crypto.adapters.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import se.ai.crypto.adapters.api.dto.ErrorResponseDto;
import se.ai.crypto.core.exception.CryptoUnsupportedException;
import se.ai.crypto.core.exception.DatabaseException;
import se.ai.crypto.core.exception.ResourceNotFoundException;
import se.ai.crypto.core.exception.StatisticTypeNotSupportedException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CryptoUnsupportedException.class)
    ResponseEntity<ErrorResponseDto> handleCryptoUnsupportedException(CryptoUnsupportedException ex) {

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .type(CryptoUnsupportedException.class.getSimpleName())
                .build()
        );
    }

    @ExceptionHandler(DatabaseException.class)
    ResponseEntity<ErrorResponseDto> handleDatabaseException(DatabaseException ex) {

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .type(CryptoUnsupportedException.class.getSimpleName())
                .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .type(CryptoUnsupportedException.class.getSimpleName())
                .build()
        );
    }

    @ExceptionHandler(StatisticTypeNotSupportedException.class)
    ResponseEntity<ErrorResponseDto> handleStatisticTypeNotSupportedException(StatisticTypeNotSupportedException ex) {

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .type(CryptoUnsupportedException.class.getSimpleName())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDto> handleException(Exception ex) {

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .type(CryptoUnsupportedException.class.getSimpleName())
                .build()
        );
    }
}
