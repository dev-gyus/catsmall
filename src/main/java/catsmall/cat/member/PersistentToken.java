package catsmall.cat.member;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
@Getter @Setter
public class PersistentToken {

    @NotBlank
    @Length(max = 64)
    private String username;

    @Id
    @Length(max = 64)
    private String series;

    @Length(max = 64)
    @NotBlank
    private String token;

    @NotBlank
    private LocalDateTime last_used;
}
