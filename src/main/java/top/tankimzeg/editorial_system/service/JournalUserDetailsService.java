package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.tankimzeg.editorial_system.entity.JournalUserDetails;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.repository.UserRepo;

/**
 * @author Kim
 * @date 2026/1/20 18:00
 * @description 期刊用户详情服务
 */
@Service
public class JournalUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户未找到: " + username));

        return JournalUserDetails.build(user);
    }

    
}
