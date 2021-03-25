package catsmall.cat.review.query;

import catsmall.cat.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewQueryRepository {
    Page<Review> findReviewsByItemId(Long itemId, Pageable pageable);
}
