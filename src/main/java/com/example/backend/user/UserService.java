package com.example.backend.user;

import com.example.backend.user.model.User;
import com.example.backend.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepository.findByEmail(username);

        if (result.isPresent()) {
            // 7번 로직
            User user = result.get();
            return user;
        }

        return null;
    }

    public void signup(UserDto.SignupRequest dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        userRepository.save(dto.toEntity(encodedPassword));
    }
}
