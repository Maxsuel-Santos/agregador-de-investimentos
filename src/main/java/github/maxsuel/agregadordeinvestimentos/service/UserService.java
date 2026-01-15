package github.maxsuel.agregadordeinvestimentos.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(
            createUserDto.username(), 
            createUserDto.email(), 
            createUserDto.password()); 

        var userSaved = userRepository.save(entity);

        log.info("User created with ID: {}", userSaved.getUserId());

        return userSaved.getUserId();
    }

}
