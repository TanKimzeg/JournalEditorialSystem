package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/23 18:57
 * @description 评审提交请求
 */
@Data
public class ReviewDTO {
    private String decision;
    private String comments;
}
