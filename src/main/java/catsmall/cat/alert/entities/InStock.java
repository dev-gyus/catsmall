package catsmall.cat.alert.entities;

import catsmall.cat.alert.AlertKinds;
import catsmall.cat.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class InStock extends Alert{
    private Long itemId;

    public InStock(Member member, AlertKinds alertKinds, Long itemId) {
        super(member, alertKinds);
        this.itemId = itemId;
    }
}
