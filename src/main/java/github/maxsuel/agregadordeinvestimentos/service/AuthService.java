package github.maxsuel.agregadordeinvestimentos.service;

import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import github.maxsuel.agregadordeinvestimentos.config.TokenService;
import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginResponseDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.entity.enums.Role;
import github.maxsuel.agregadordeinvestimentos.exceptions.UserNotFoundException;
import github.maxsuel.agregadordeinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UUID register(@NonNull CreateUserDto createUserDto) {
        var entity = new User(
                createUserDto.username(),
                createUserDto.email(),
                passwordEncoder.encode(createUserDto.password()),
                Role.ADMIN
        );

        var userSaved = userRepository.save(entity);

        log.info("User created with ID: {} and type {}", userSaved.getUserId(), userSaved.getRole());

        return userSaved.getUserId();
    }

    @Transactional
    public LoginResponseDto login(@NonNull LoginDto dto) {
        var user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid username or password");
        }

        var token = tokenService.generateToken(user);
        return new LoginResponseDto(token);
    }

}
