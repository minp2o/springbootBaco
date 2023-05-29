package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity     //해당 클래스가 데이터베이스의 테이블과 매핑되는 엔티티 클래스임
@Getter     //클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성. 이를 통해 필드의 값을 외부에서 읽을 수 있게됨.
@NoArgsConstructor(access = PROTECTED)
public class Member {       //Member 클래스 정의
    @Id @GeneratedValue     //엔티티 클래스의 PK 필드를 나타냄, 자동으로 식별자 필드의 값을 생성하는 방법을 지정함
    private Long id;        //id 선언

    private String name;    //name 선언

    @OneToMany(mappedBy = "member")     //member 필드를 기준으로 Order 엔티티와의 연관 관계를 매핑. mappedby는 member가 주인임을 뜻함
    private List<Order> orderList = new ArrayList<>();  //Order 엔티티의 목록을 담는 orderList 필드 정의

    @Embedded       //Address 객체가 Member 엔티티에 속한 멤버 변수로 사용됨
    private Address address;

    public static Member createMember(String name, Address address) {   //name과 address를 받아 Member 객체를 생성하여 반환
        Member member = new Member();   //Member 객체 생성
        member.name = name;     //받은 name을 member객체의 name필들에 할당
        member.address = address; //받은 address을 member객체의 address필들에 할당
        return member;      //생성된 member 객체 반환
    }
}
