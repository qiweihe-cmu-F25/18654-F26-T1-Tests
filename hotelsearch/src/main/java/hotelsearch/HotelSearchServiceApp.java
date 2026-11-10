package hotelsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelSearchServiceApp {
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


        SpringApplication.run(HotelSearchServiceApp.class, args);
    }

}
