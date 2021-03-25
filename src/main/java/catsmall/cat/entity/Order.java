package catsmall.cat.entity;

import catsmall.cat.delivery.Delivery;
import catsmall.cat.member.Member;
import catsmall.cat.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    private LocalDateTime orderDate;
    private boolean reviewed;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;


    public Order(Member member,Delivery delivery){
        setMember(member);
        setDelivery(delivery);
        orderDate = LocalDateTime.now();
        orderStatus = OrderStatus.ORDER;
    }

    // Business Method
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        orderItem.subtractItem();
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    //    public void cancelOrder(OrderItem orderItem){
//        orderStatus = OrderStatus.CANCEL;
//        deleteOrderItem(orderItem);
//    }
    public void cancelOrderItem(OrderItem orderItem){
        orderItem.getItem().addQuantity(orderItem.getCount());
        deleteOrderItem(orderItem);
    }

    private void deleteOrderItem(OrderItem orderItem){
        this.getOrderItems().removeIf(oi -> {
            if(oi.getId() == orderItem.getId())
                return true;
            return false;
        });
    }
}
