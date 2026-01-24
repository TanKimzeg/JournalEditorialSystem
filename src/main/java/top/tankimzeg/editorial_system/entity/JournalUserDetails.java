package top.tankimzeg.editorial_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/20 17:55
 * @description Journal User Details 实现 UserDetails 接口
 */
@Getter
public class JournalUserDetails implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String realName;

    private User.Role role;

    private Collection<? extends GrantedAuthority> authorities;

    public JournalUserDetails(
            Long id,
            String username,
            String password,
            String email,
            String realName,
            User.Role role,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.realName = realName;
        this.role = role;
        this.authorities = authorities;
    }

    public static JournalUserDetails build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        if (user.getRole().equals(User.Role.EDITOR))
            authorities.add(new SimpleGrantedAuthority("ROLE_" + User.Role.AUTHOR));

        return new JournalUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRealName(),
                user.getRole(),
                authorities
        );
    }

}
