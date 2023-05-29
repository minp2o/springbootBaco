package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static likelion.springbootBaco.domain.Delivery.DeliveryStatus.ESTABLISHED;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Delivery {
    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery")    //Deliver와 Order의 일대일 관계 매핑
    private Order order;    //delivery 필드가 주인

    @Enumerated(STRING) //열거형(Enum) 타입의 매핑을 지정하는 데 사용됨. 여기서는 DeliveryStatus 열거형을 매핑함.
    private DeliveryStatus deliveryStatus;

    private String city;
    private String state;
    private String street;
    private String zipcode;

    public static Delivery createDelivery(Order order, String city, String state, String street, String zipcode) {  //createDelivery 메서드로 Order객체와 배송지 정보를 받아 Delivery객체 생성 후 반환
        Delivery delivery = new Delivery(); //Delivery 객체 생성
        delivery.order = order;
        delivery.deliveryStatus = ESTABLISHED; //deliveryStateus 필드에 열거형 값을 보여줌
        delivery.city = city;
        delivery.state = state;
        delivery.street = street;
        delivery.zipcode = zipcode;     //delivery객체에 배송지 정보 받아와서 저장
        return delivery;        //delivery 객체 반환
    }

    public enum DeliveryStatus {
        ESTABLISHED, PROGRESS, DONE
    }
}   //DeliveryStatus 열거형에 세 개의 상수 정의
