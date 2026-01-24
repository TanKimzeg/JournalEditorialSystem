package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/21 11:58
 * @description 编辑决策 DTO
 */
@Data
public class EditorDecisionDTO {
    private String decision; // "ACCEPT", "REJECT", "REVISE"
    private String comments; // 主编的评审意见
}
