package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User createAuthor(User user) {
        return userRepo.save(user);
    }

    public User updateAuthor(User userDetail) {
        User user = userRepo.findById(userDetail.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsUserByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
