package catsmall.cat.delivery;

import catsmall.cat.delivery.DeliveryStatus;
import catsmall.cat.entity.Order;
import catsmall.cat.member.Address;
import catsmall.cat.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private Address address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    @JsonIgnore
    private Order order;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public Delivery(Address address, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.order = order;
        this.deliveryStatus = deliveryStatus;
    }
}
