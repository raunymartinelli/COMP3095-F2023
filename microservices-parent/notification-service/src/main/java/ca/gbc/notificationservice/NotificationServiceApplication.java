package ca.gbc.notificationservice;

import ca.gbc.notificationservice.events.OrderPlaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@KafkaListener(topics= "notificationTopic", groupId = "notificationGroup")
	public void handleNotification(OrderPlaceEvent orderPlaceEvent){
		log.info("Sending out email notification for order number : {}",
				orderPlaceEvent.getOrderNumber());
	}


}
