package com.tantai.uristudy.security;

import com.tantai.uristudy.constant.Role;
import com.tantai.uristudy.entity.User;
import com.tantai.uristudy.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        UserDetails userDetails = CustomUserDetails.builder()
                .user(user)
                .authorities(List.of(new SimpleGrantedAuthority(Role.USER.name())))
                .build();

        return userDetails;
    }
}
