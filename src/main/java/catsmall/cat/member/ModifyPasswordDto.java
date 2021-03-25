package catsmall.cat.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModifyPasswordDto {
    @NotBlank
    private String password;
    @NotBlank
    private String passwordRepeat;
}
