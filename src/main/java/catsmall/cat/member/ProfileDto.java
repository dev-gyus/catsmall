package catsmall.cat.member;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ProfileDto {

    @NotBlank
    private String nickname;
    @NotBlank
    private String phonenum;
    @NotBlank
    private String name;
    @Lob
    private String profileImage;

}
