package com.infraredcoding.cubesturm.services;

import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class SturmUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SturmUser> user = userRepository.findByUsername(username);
        return user.map(u -> new User(
                u.getUsername(), u.getPassword(),
                true, true, true,
                true, List.of(new SimpleGrantedAuthority("cuber"))
        )).orElseThrow(() -> new UsernameNotFoundException("SturmUser not found"));
    }
}
