package academy.devdojo.springboot.domain.ExceptionHandler;

import academy.devdojo.springboot.domain.Exception.ResourceNotFoundDetails;
import academy.devdojo.springboot.domain.Exception.ResourcesNotFoundException;
import academy.devdojo.springboot.domain.Exception.ValidationExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler  {
    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDetails> handleResourcesNotFoundException(ResourcesNotFoundException resNotFoundExp) {
        return new ResponseEntity<>(ResourceNotFoundDetails.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource Not Found")
                .detail(resNotFoundExp.getMessage())
                .developerMessage(resNotFoundExp.getClass().getName())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handleValidationExceptionDetails(MethodArgumentNotValidException exp) {
        List<FieldError> fieldError = exp.getBindingResult().getFieldErrors();
        String fields = fieldError.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMsg = fieldError.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Field Validation Error")
                        .detail("Check The Field(s) Below")
                        .developerMessage(exp.getClass().getName())
                        .fields(fields)
                        .filedMessage(fieldMsg)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
