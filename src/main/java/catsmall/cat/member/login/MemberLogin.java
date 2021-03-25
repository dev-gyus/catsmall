package catsmall.cat.member.login;

import catsmall.cat.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class MemberLogin extends User {
    private Member member;

    public MemberLogin(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole().toString())));
        this.member = member;
    }
}
