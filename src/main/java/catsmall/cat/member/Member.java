package catsmall.cat.member;

import catsmall.cat.admin.Roles;
import catsmall.cat.alert.entities.Alert;
import catsmall.cat.cart.Cart;
import catsmall.cat.entity.Order;
import catsmall.cat.zzim.Zzim;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Column(unique = true)
    private String nickname;

    @NotBlank
    private String name;
    @NotBlank
    private String phonenum;
    @Lob
    private String profileImage;

    private String emailToken;

    private boolean isEmailChecked;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.ROLE_USER;

    private LocalDateTime regDate = LocalDateTime.now();

    @Embedded
    private Address address;

    private boolean isBlocked;
    private boolean isLocked;

    private Integer loginFailedCount = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "zzim_id")
    private Zzim zzim;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alert> alertList;

    public Member(String email, String password, String name, String nickname,String phonenum, Address address, Roles role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phonenum = phonenum;
        this.address = address;
        this.role = role;
        this.nickname = nickname;
    }

    // business method
    public static Member getMember(MemberDto memberDto){
        return new Member(memberDto.getEmail(), memberDto.getPassword(),
                memberDto.getName(), memberDto.getNickname(), memberDto.getPhonenum(),
                new Address(memberDto.getPostcode(), memberDto.getRoadAddress(), memberDto.getJibunAddress(), memberDto.getDetailAddress(), memberDto.getExtraAddress()),
                memberDto.getRole());
    }

    public void addAlert(Alert alert){
        this.alertList.add(alert);
        alert.setMember(this);
    }
}
