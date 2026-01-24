package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tankimzeg.editorial_system.dto.request.AuthorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.AuthorProfileVO;
import top.tankimzeg.editorial_system.entity.AuthorProfile;
import top.tankimzeg.editorial_system.mapper.AuthorProfileMapper;
import top.tankimzeg.editorial_system.repository.AuthorProfileRepo;
import top.tankimzeg.editorial_system.repository.UserRepo;

/**
 * @author Kim
 * @date 2026/1/21 10:33
 * @description 作者资料服务
 */
@Service
public class AuthorProfileService {

    @Autowired
    private AuthorProfileRepo authorProfileRepo;

    @Autowired
    private UserRepo userRepo;

    private static final AuthorProfileMapper authorProfileMapper = AuthorProfileMapper.INSTANCE;

    @Transactional
    public AuthorProfileVO createAuthorProfile(Long authorId, AuthorProfileDTO profileDTO) {
        AuthorProfile authorProfile = authorProfileMapper.dtoToEntity(profileDTO,
                userRepo.findById(authorId).orElseThrow(
                        () -> new RuntimeException("用户不存在")
                ));
        return authorProfileMapper.entityToVO(authorProfileRepo.save(authorProfile));
    }

    public AuthorProfileVO getAuthorProfileById(Long id) {
        return authorProfileMapper.entityToVO(authorProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("作者资料不存在")));
    }

    @Transactional
    public AuthorProfileVO patchAuthorProfile(Long authorId, AuthorProfileDTO patchedProfile) {
        AuthorProfile existingProfile = authorProfileRepo.findById(authorId)
                .orElseThrow(
                        () -> new RuntimeException("作者资料不存在")
                );
        authorProfileMapper.updateEntityFromDTO(patchedProfile, existingProfile);
        return authorProfileMapper.entityToVO(authorProfileRepo.save(existingProfile));
    }

    @Transactional
    public void deleteAuthorProfile(Long id) {
        authorProfileRepo.deleteById(id);
    }

}
