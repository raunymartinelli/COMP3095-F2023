package ca.gbc.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderPlaceEvent {

    private String orderNumber;
}
