package catsmall.cat.review;

import catsmall.cat.review.query.ReviewQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
}
