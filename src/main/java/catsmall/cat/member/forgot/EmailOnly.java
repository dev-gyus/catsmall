package catsmall.cat.member.forgot;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailOnly {
    @NotBlank
    @Email
    private String email;
}
