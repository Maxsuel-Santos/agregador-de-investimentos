package github.maxsuel.agregadordeinvestimentos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import github.maxsuel.agregadordeinvestimentos.dto.CreateAccountDto;
import github.maxsuel.agregadordeinvestimentos.entity.Account;
import github.maxsuel.agregadordeinvestimentos.entity.BillingAddress;
import github.maxsuel.agregadordeinvestimentos.repository.AccountRepository;
import github.maxsuel.agregadordeinvestimentos.repository.BillingAddressRepository;
import org.springframework.stereotype.Service;

import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.UpdateUserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.exceptions.UserNotFoundException;
import github.maxsuel.agregadordeinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final BillingAddressRepository billingAddressRepository;

    @Transactional
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

    @Transactional
    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var userUuid = UUID.fromString(userId);

        var user = userRepository.findById(userUuid)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (updateUserDto.username() != null) {
            user.setUsername(updateUserDto.username());
        }

        if (updateUserDto.password() != null) {
            user.setPassword(updateUserDto.password());
        }

        userRepository.save(user);
        log.info("User updated with ID: {}", userId);
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

    @Transactional
    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        var account = new Account(
                user,
                createAccountDto.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}
