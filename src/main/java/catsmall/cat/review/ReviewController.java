package catsmall.cat.review;

import catsmall.cat.admin.ImageUpload;
import catsmall.cat.entity.Order;
import catsmall.cat.entity.item.Item;
import catsmall.cat.member.CurrentUser;
import catsmall.cat.member.Member;
import catsmall.cat.order.OrderRepository;
import catsmall.cat.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final OrderRepository orderRepository;


    @GetMapping("/{orderId}/add")
    public String reviewAdd(@CurrentUser Member member, @PathVariable Long orderId,
                            @ModelAttribute ReviewDto reviewDto, Model model){
        // TODO 오더가 지금 로그인한 멤버것인지 확인로직 구현
        Order findOrder = orderRepository.findItemDeliveryById(orderId);
        List<Item> itemList = findOrder.getOrderItems().stream().map(oi -> oi.getItem()).collect(Collectors.toList());
        model.addAttribute("itemList", itemList);
        model.addAttribute("orderId", findOrder.getId());
        return "review/add";
    }

    @PostMapping("/{orderId}/add")
    public String reviewAdd_do(@CurrentUser Member member, @PathVariable Long orderId,
                               @ModelAttribute ReviewDto reviewDto,Model model){
        reviewService.addReview(member, orderId, reviewDto);
        return "redirect:/order";
    }
    @PostMapping("/{reviewId}/delete")
    public String reviewDelete_do(@CurrentUser Member member, @PathVariable Long reviewId,
                                  @RequestParam Long item, HttpServletRequest request){
        if(member == null){
            throw new IllegalArgumentException("올바른 접근이 아닙니다.");
        }
        reviewService.removeReview(member, reviewId);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/api/image")
    @ResponseBody
    public ImageUpload review_imageUpload(@RequestBody MultipartFile image){
        String fileName = reviewService.uploadReviewImage(image);
        ImageUpload imageUpload = new ImageUpload();
        imageUpload.setUrl(fileName);
        return imageUpload;
    }
}
