package academy.devdojo.springboot.domain.Exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionsDetails {
    private String fields;
    private String filedMessage;
}
