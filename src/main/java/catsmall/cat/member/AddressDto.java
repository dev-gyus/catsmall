package catsmall.cat.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressDto {
    @NotBlank
    private String postcode;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String jibunAddress;
    @NotBlank
    private String detailAddress;

    private String extraAddress;
}
