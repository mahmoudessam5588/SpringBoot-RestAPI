package academy.devdojo.springboot.domain.Controller;

import academy.devdojo.springboot.domain.Util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("dateTimes")
@Slf4j
@RequiredArgsConstructor
public class DateTimeController {
    @Autowired
    private final Utils utils;
    @GetMapping
    public void timeNow(){
        log.info("Time Now and Then : " + utils.FormattingLocalDate(LocalDateTime.now()));
    }
}
