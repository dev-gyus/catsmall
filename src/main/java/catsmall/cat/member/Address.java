package catsmall.cat.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Target;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;

    private String extraAddress;

    public Address(String postcode, String roadAddress, String jibunAddress, String detailAddress, String extraAddress) {
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }
}
