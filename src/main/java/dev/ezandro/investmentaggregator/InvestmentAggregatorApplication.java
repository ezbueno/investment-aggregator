package dev.ezandro.investmentaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InvestmentAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvestmentAggregatorApplication.class, args);
    }
}