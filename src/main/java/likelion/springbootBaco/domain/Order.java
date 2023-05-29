package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "orders") // 이거 안하면 에러
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 이 필드는 Order와 Member 클래스 간의 연관관계를 설정하기 위한 어노테이션으로 이루어져 있습니다.
     * @ManyToOne 은 "Order : Member = 여러개 : 1" 관계 (== N : 1)임을 나타내고 있습니다.
     * 또한 @ManyToOne의 속성 중 하나인 fetch는 지연로딩을 사용하기 위해 LAZY로 되어 있습니다.(이 부분은 한번 찾아보시고, 이해 안되면 일단은 넘어가셔도 됩니다.)
     * @JoinColumn은 연관관계를 풀어내는 방식을 "FK Column으로 넣어주겠다."는 설정이며, name을 설정한건 Order테이블의 해당 FK 칼럼 이름을 member_id로 설정하겠단 의미입니다.
     * 그리고 접근제어자를 private으로 설정함으로써 해당 필드에 대한 접근 포인트를 클래스 내부로 최소화함으로써 안정적인 구조를 취하였습니다.
     * >>> 위 어노테이션 내용은 우선은 ManyToOne, OneToOne, OneToMany만 구별할 줄 알면 되고,
     * >>> fetch와 JoinColumn은 이해하기 어려울 수 있습니다. 이 부분은 구글링 해보시되, 어려우면 공식처럼 사용하는구나 만 인지하고 있으면 됩니다.
     * >>> 단, ManyToOne 등의 어노테이션 설정시 어느 클래스에 ManyToOne을 하고, 어느 클래스에 @OneToMany를 하는지 그 연관관계에 대해서는 이해해야 합니다.
     * >>> 또한 연관관계의 주인에 JoinColumn을 사용하고, 연관관계의 하인(?)에는 mappedBy를 사용한다는 개념정도는 이해해야 합니다.
     * >>> 외울 것도 없어요. 나중에 해커톤 코딩할 때 우리가 작성한 코드 보면서 참고하면 되니까. 직접 쓰다보면 알아서 외워지고 익숙해집니다.
     */


    //domain 패키지 내 다른 파일들에서 모든 설명을 다 했기에 생략
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY, cascade = ALL) //cascade 속성은 연관된 엔티티에 대해 수행되는 작업을 설정하는데 사용됨. CascadeType.ALL은 모든 작업(예: 저장, 업데이트, 삭제 등)에 대해 연관된
    // 엔티티에도 동일한 작업이 전파되도록 설정. 따라서, 해당 관계에 속하는 엔티티에 대해 수행되는 작업이 전파되어 연관된 엔티티도 동일한 작업이 수행됨.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrderList().add(this);
    }

    public static Order createOrder(Member member, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.orderDate = LocalDateTime.now();
        order.orderStatus = OrderStatus.ORDERED;
        order.delivery = Delivery.createDelivery(order, member.getAddress().getCity(),
                member.getAddress().getState(),
                member.getAddress().getStreet(),
                member.getAddress().getZipcode());
        for (OrderItem orderItem : orderItems) {
            order.orderItemList.add(orderItem);
            orderItem.setOrder(order);
        }
        return order;
    }

    public void cancel() {
        if (delivery.getDeliveryStatus() == Delivery.DeliveryStatus.DONE) {
            throw new IllegalStateException("배송 완료했다 양아치야");
        }
        this.orderStatus = OrderStatus.CANCELED;
        for (OrderItem orderItem : orderItemList) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
