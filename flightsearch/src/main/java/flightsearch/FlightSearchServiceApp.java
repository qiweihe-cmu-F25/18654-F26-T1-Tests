package flightsearch;

import flightsearch.gateway.FlightsApiGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FlightSearchServiceApp {
    public static void main(String[] args) {
//
//        // Annotation based spring context
//        AnnotationConfigApplicationContext context
//                = new AnnotationConfigApplicationContext();
//        context.scan("flightsearch");
//        context.refresh();
//
//        // Getting the Bean from the component class
//        FlightsApiGateway searchApiGateway
//                = context.getBean(FlightsApiGateway.class);
//        searchApiGateway.init();
//
//        // Closing the context
//        // using close() method
//        context.close();


        SpringApplication.run(FlightSearchServiceApp.class, args);
    }

}
