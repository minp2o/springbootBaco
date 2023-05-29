package likelion.springbootBaco.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItem = new ArrayList<>();

    private String brand;
    private String name;
    private Integer price;
    private Integer stock;

    /**
     * 비즈니스 로직
     */
    @Comment("재고 추가")       //주석 어노테이션
    public void addStock(int quantity) {
        this.stock += quantity;
    }   //addStock메서드로 quantity 라는 매개변수를 받아 stock필드에 해당 수량을 증가

    @Comment("재고 감소")
    public void removeStock(int stockQuantity) {    //stockQuantity라는 매개변수를 받는 removeStock 메서드 실행
        int restStock = this.stock - stockQuantity; //stock필드의 수량에 stockQuantity를 뺀 값을 restStock으로 저장
        if (restStock < 0) {
            throw new IllegalStateException("need more stock");
        }
        this.stock = restStock;
    }
}
