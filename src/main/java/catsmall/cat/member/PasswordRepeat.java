package catsmall.cat.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordRepeat {
    @NotBlank
    private String password;
}
