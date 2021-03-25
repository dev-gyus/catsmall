package catsmall.cat.review;

import catsmall.cat.config.AppProperties;
import catsmall.cat.entity.Order;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.Member;
import catsmall.cat.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final AppProperties appProperties;

    @Transactional
    public void addReview(Member member, Long orderId, ReviewDto reviewDto) {
        Order findOrder = orderRepository.findItemDeliveryById(orderId);
        List<Item> itemList = findOrder.getOrderItems().stream().map(oi -> oi.getItem()).collect(Collectors.toList());
        for(int a=0; a < itemList.size(); a++) {
            // TODO ReviewDto 내부 필드 List로 받아올것
            for(int b=0; b < 100; b++) {
                Review review = new Review(member, itemList.get(a), reviewDto.getContent().get(a), reviewDto.getStarPoint().get(a));
                review.setContent(b + "번째 테스트 리뷰");
                reviewRepository.save(review);
                findOrder.setReviewed(true);
            }
        }
    }

    public String uploadReviewImage(MultipartFile image) {
        String imgPrefix = appProperties.getImgPrefix();
        String fileType = image.getContentType().substring(0, image.getContentType().indexOf('/'));
        if(!fileType.equals("image")){
            throw new IllegalArgumentException("잘못된 파일형식입니다.");
        }
        File directory = new File(imgPrefix + "review/");
        if(!directory.exists()){
            directory.mkdirs();
        }
        String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "_" + LocalDateTime.now() + "." + extension;
        File settingFile = new File(directory, fileName);
        try {
            image.transferTo(settingFile);
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드 실패");
        }
        return appProperties.getHost() + "/upload/review/" + fileName;
    }

    @Transactional
    public void removeReview(Member member, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        if(!review.getMember().getId().equals(member.getId())){
            throw new IllegalArgumentException("올바른 접근이 아닙니다.");
        }
        reviewRepository.delete(review);
    }
}
