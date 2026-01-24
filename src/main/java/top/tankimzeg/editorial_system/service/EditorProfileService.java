package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tankimzeg.editorial_system.dto.request.EditorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.EditorProfileVO;
import top.tankimzeg.editorial_system.entity.EditorProfile;
import top.tankimzeg.editorial_system.mapper.EditorProfileMapper;
import top.tankimzeg.editorial_system.repository.EditorProfileRepo;
import top.tankimzeg.editorial_system.repository.UserRepo;

/**
 * @author Kim
 * @date 2026/1/21 10:48
 * @description 编辑资料服务
 */
@Service
public class EditorProfileService {

    @Autowired
    private EditorProfileRepo editorProfileRepo;

    @Autowired
    private UserRepo userRepo;

    private static final EditorProfileMapper editorProfileMapper = EditorProfileMapper.INSTANCE;

    @Transactional
    public EditorProfileVO createEditorProfile(Long editorId, EditorProfileDTO profileDTO) {
        EditorProfile newProfile = editorProfileMapper.dtoToEntity(profileDTO,
                userRepo.findById(editorId).orElseThrow(
                        () -> new RuntimeException("用户不存在")
                ));
        return editorProfileMapper.entityToVO(editorProfileRepo.save(newProfile));
    }

    public EditorProfileVO getEditorProfileById(Long id) {
        return editorProfileMapper.entityToVO(editorProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("作者资料不存在")));
    }

    public EditorProfileVO patchEditorProfile(Long editorId, EditorProfileDTO patchedProfile) {
        EditorProfile existingProfile = editorProfileRepo.findById(editorId).orElseThrow(
                () -> new RuntimeException("用户不存在")
        );
        editorProfileMapper.updateEntityFromDTO(patchedProfile, existingProfile);
        return editorProfileMapper.entityToVO(editorProfileRepo.save(existingProfile));
    }

    public void deleteEditorProfile(Long id) {
        editorProfileRepo.deleteById(id);
    }

}
