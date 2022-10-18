package academy.devdojo.springboot.domain.Exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionsDetails {
    protected String title;
    protected Integer status;
    protected String detail;
    protected LocalDateTime timeStamp;
    protected String developerMessage;
}
