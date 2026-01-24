package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tankimzeg.editorial_system.entity.EditorProfile;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.repository.EditorProfileRepo;
import top.tankimzeg.editorial_system.repository.UserRepo;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/20 11:40
 * @description 期刊编辑服务类
 */
@Service
public class EditorService {

    @Autowired
    EditorProfileRepo editorProfileRepo;

    @Autowired
    UserRepo userRepo;

    public List<EditorProfile> getAllEditors() {
        return editorProfileRepo.findAll();
    }

    public EditorProfile getEditorById(Long editorId) {
        return editorProfileRepo.findById(editorId)
                .orElseThrow(() -> new RuntimeException("编辑不存在"));
    }

    public User assignEditorToInitialReview(Long manuscriptId) {
        // 目前为简化逻辑，随机分配一个编辑
        List<EditorProfile> editors = getAllEditors();
        if (editors.isEmpty()) {
            throw new RuntimeException("没有可用的编辑来处理稿件");
        } else {
            return userRepo.getReferenceById(
                    editors.get((int) (Math.random() * editors.size())).getId()
            );
        }
    }

    public User assignEditorToSelfRecommendation() {
        // 目前为简化逻辑，随机分配一个编辑
        List<EditorProfile> editors = getAllEditors();
        if (editors.isEmpty()) {
            throw new RuntimeException("没有可用的编辑来处理自荐申请");
        } else {
            return userRepo.getReferenceById(
                    editors.get((int) (Math.random() * editors.size())).getId()
            );
        }
    }
}
