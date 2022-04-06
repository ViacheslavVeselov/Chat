package bvvs.chatserver.security.jwt;

import bvvs.chatserver.models.Role;
import bvvs.chatserver.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class JwtUserDetails implements UserDetails {
    private final UUID id;
    private final String password;
    private final String email;
    private final boolean banned;
    private final Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static JwtUserDetails from(User user) {
        return new JwtUserDetails(user.getId(), user.getPassword(), user.getEmail(), user.isBanned(),
                Collections.singletonList(Role.getAuthority(user)));
    }
}
