package catsmall.cat.review;

import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.jdo.annotations.Join;
import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Review {
    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private String content;

    private Integer starPoint;

    public Review(Member member, Item item, String content, Integer starPoint) {
        this.member = member;
        this.content = content;
        this.starPoint = starPoint;
        this.item = item;
    }
}
