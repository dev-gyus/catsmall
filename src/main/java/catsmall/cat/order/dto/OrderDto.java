package catsmall.cat.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderDto {
    private Long id;
    @NotBlank
    private int count;
    @NotBlank
    private String postcode;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String jibunAddress;
    @NotBlank
    private String detailAddress;

    public void setForOrder(@NotBlank String postcode, @NotBlank String roadAddress,
                            @NotBlank String jibunAddress, @NotBlank String detailAddress, int count) {
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
    }
}
