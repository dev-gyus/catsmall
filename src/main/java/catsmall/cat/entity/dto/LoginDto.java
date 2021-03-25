package catsmall.cat.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
