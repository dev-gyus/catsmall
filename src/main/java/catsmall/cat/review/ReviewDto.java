package catsmall.cat.review;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

@Data
public class ReviewDto {
    private List<Integer> starPoint;
    private List<String> content;
}
