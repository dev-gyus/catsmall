package catsmall.cat.member;

import catsmall.cat.admin.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phonenum;
    @NotBlank
    private String postcode;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String jibunAddress;
    @NotBlank
    private String detailAddress;
    private String extraAddress;

    private Roles role = Roles.ROLE_USER;

    private Address address;

    public MemberDto(String email, String password, String name, String nickname, String phonenum, Address address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phonenum = phonenum;
        this.address = address;
        this.nickname = nickname;
    }
    public MemberDto(Member member) {
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.phonenum = member.getPhonenum();
        this.address = member.getAddress();
        this.nickname = member.getNickname();
    }
}
