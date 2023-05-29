package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)        //다대일 관계 매핑, 지연 로딩을 사용하여 연관된 Order객체를 필요할 때 가져옴.
    @JoinColumn(name = "order_id")      //order_id라는 컬럼을 사용해 Member테이블과 order테이블의 조인 컬럼 지정
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;      //OrderItem 테이블과 Item테이블 조인

    private Integer price;      //price, count 필드 정의
    private Integer count;

    /**
     * 스태틱 팩토리 메서드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int orderCount) {    //Item,orderPrice,orderCount를 받고 OrderItem 객체 생성해 반환
        OrderItem orderItem = new OrderItem();  //OrderItem 객체 생성
        orderItem.setItem(item);    //받은 Item 객체를 orderItem 객체의 setItem 메서드를 통해 설정
        orderItem.price = orderPrice;   //orderPrice를 price필드에 할당
        orderItem.count = orderCount;   //orderCount를 count필드에 할당
        // 연관관계 편의 메서드
        item.removeStock(orderCount);   //removeStock메서드를 사용하여 orderCount만큼 Item객체의 stock을 감소시킴
        return orderItem;       //orderItem 반환
    }

    public void setOrder(Order order) { //Order 객체를 받아 Oder 객체의 OrderItemList에 OrderItem 객체 추가
        this.order = order;     //Order 객체를 order필드에 할당
        order.getOrderItemList().add(this); //getOrderItemList메서드로 OrderItemList를 가져와 OrderItem객체를 추가함
    }

    public void setItem(Item item) {
        this.item = item;
        item.getOrderItem().add(this);
    }

    /**
     * 비즈니스 로직
     */
    public int getTotalPrice() {
        return this.getPrice() * this.getCount();
    }
    //getTotalPrice 메서드를 이용하여 가격*수량으로 반환

    public void cancel() {
        this.getItem().addStock(count);
    }
    //cancel 메서드를 이용하여 재고 추가
}
