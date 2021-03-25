package catsmall.cat.admin.manage.dto;

import catsmall.cat.admin.Roles;
import catsmall.cat.member.Member;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class MemberManageDto {
    private String email;

    private String nickname;

    private String name;

    private String phonenum;

    private Roles role;

    private LocalDateTime regDate = LocalDateTime.now();

    private boolean isBlocked;
}
