package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.exceptions.DuplicatedDataException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import github.maxsuel.agregadordeinvestimentos.config.TokenService;
import github.maxsuel.agregadordeinvestimentos.dto.AuthResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.dto.UserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.entity.enums.Role;
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
    public AuthResponseDto register(@NonNull CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new DuplicatedDataException("The username already exists.");
        }

        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new DuplicatedDataException("Email already registered");
        }

        var entity = new User(
                createUserDto.username(),
                createUserDto.email(),
                passwordEncoder.encode(createUserDto.password()),
                Role.ADMIN
        );


        var userSaved = userRepository.save(entity);

        log.info("User created with ID: {} and type {}", userSaved.getUserId(), userSaved.getRole());

        var token = tokenService.generateToken(entity);
        UserDto userDto = new UserDto(
                userSaved.getUserId().toString(),
                userSaved.getUsername(),
                userSaved.getEmail(),
                userSaved.getRole()
        );

        return new AuthResponseDto(token, userDto);
    }

    @Transactional
    public AuthResponseDto login(@NonNull LoginDto dto) {
        var user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDto userDto = new UserDto(
                user.getUserId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        var token = tokenService.generateToken(user);
        return new AuthResponseDto(token, userDto);
    }

}
