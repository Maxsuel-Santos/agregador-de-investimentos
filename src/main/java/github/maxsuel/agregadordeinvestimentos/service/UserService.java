package github.maxsuel.agregadordeinvestimentos.service;

import java.util.List;
import java.util.Optional;
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

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        var userUuid = UUID.fromString(userId);

        var userExists = userRepository.existsById(userUuid);

        if (userExists) {
            userRepository.deleteById(userUuid);
            log.info("User deleted with ID: {}", userId);
        } else {
            log.warn("Attempted to delete non-existing user with ID: {}", userId);
        }
    }

}
