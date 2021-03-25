package catsmall.cat.order.dto;

import catsmall.cat.delivery.DeliveryStatus;
import catsmall.cat.entity.OrderItem;
import catsmall.cat.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderMainDto {
    private Long orderId;
    private List<OrderItem> orderItemList;
    private int price;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private boolean reviewed;

    public OrderMainDto(Long orderId, List<OrderItem> orderItemList, LocalDateTime orderDate,
                        OrderStatus orderStatus, DeliveryStatus deliveryStatus, boolean reviewed) {
        this.orderId = orderId;
        this.orderItemList = orderItemList;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.reviewed = reviewed;
    }
}
