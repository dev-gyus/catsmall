package catsmall.cat.alert.entities;

import catsmall.cat.alert.AlertKinds;
import catsmall.cat.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Alert {
    @Id @GeneratedValue
    @Column(name = "alert_id")
    private Long id;

    private AlertKinds alertKinds;
    private String subject;
    private String content;
    private boolean isRead;
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Alert(Member member, AlertKinds alertKinds) {
        this.alertKinds = alertKinds;
        this.member = member;
        regDate = LocalDateTime.now();
    }
}
