package likelion.springbootBaco.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable     //해당 클래스가 다른 엔티티 클래스에 포함될 수 있음
@Data       //getter,setter와 같은 메서드 자동 생성
@AllArgsConstructor     //모든 멤버 변수를 인자로 갖는 생성자를 자동 생성
@NoArgsConstructor      //파라미터가 없는 기본 생성자 자동 생성
public class Address {      //Address 클래스 정의, private으로 필드 선언
    private String city;
    private String state;
    private String street;
    private String zipcode;
}
