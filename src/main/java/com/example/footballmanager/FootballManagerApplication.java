package com.example.footballmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class FootballManagerApplication {
  public static void main(String[] args) {
     SpringApplication.run(FootballManagerApplication.class, args);
  }
}
