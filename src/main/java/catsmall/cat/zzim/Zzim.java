package catsmall.cat.zzim;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Zzim {
    @Id @GeneratedValue
    @Column(name = "zzim_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "zzim")
    @JsonIgnore
    private Member member;

    @OneToMany
    private List<Item> itemList = new ArrayList<>();

    public Zzim(Member member) {
        this.member = member;
        member.setZzim(this);
    }

    public Zzim(Member member, Item item) {
        setMember(member);
        addItem(item);
    }

    public void addItem(Item item){
        this.itemList.add(item);
    }

    public void setMember(Member member){
        member.setZzim(this);
        this.member = member;
    }
}
