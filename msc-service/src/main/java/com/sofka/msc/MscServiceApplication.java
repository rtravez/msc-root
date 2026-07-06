package com.sofka.msc;

import com.sofka.msc.config.MscConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
@Import({MscConfiguration.class})
public class MscServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MscServiceApplication.class, args);
    }
}
