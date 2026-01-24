package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/22 22:36
 * @description 稿件信息 VO
 */
@Getter
@Setter
public class ManuscriptVO {
    private Long id;

    private String title;

    private String abstractText;

    private String keywords;

    private String fields;

    private String status;

    private List<String> authors;

    private List<String> attachmentIds;
}
