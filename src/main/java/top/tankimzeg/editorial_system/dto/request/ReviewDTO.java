package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;
import top.tankimzeg.editorial_system.entity.Review;

/**
 * @author Kim
 * @date 2026/1/23 18:57
 * @description 评审提交请求
 */
@Data
public class ReviewDTO {
    private Review.ReviewDecision decision;
    private String comments;
}
