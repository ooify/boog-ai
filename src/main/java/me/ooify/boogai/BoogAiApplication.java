package me.ooify.boogai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("me.ooify.boogai.mapper")
public class BoogAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoogAiApplication.class, args);
    }

}
