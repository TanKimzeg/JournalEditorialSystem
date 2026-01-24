package top.tankimzeg.editorial_system.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kim
 * @date 2026/1/22 20:51
 * @description 稿件 DTO
 */
@Getter
@Setter
public class ManuscriptDTO {
    private String title;

    private String abstractText;

    private String keywords;

    private String fields;

}
