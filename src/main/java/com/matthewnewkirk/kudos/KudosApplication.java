package com.matthewnewkirk.kudos;

import com.matthewnewkirk.kudos.db.TestH2Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(value = {"classpath:spring-config.xml"})
public class KudosApplication {

    public static void main(String[] args) {
      SpringApplication.run(KudosApplication.class, args);
    }
}
