package academy.devdojo.springboot.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class applicationStart  {
    public static void main(String[] args) {
        SpringApplication.run(applicationStart.class,args);
    }
}
