package github.maxsuel.agregadordeinvestimentos.repository;

import github.maxsuel.agregadordeinvestimentos.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("User Repository Test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should get user successfully from DB")
    public void findByIdSuccess() {
        // Arrange
        User user = new User("username", "username@email.com", "123");
        entityManager.persist(user);

        // Act
        Optional<User> result = userRepository.findById(user.getUserId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("Should return empty when user not exists")
    public void findByIdNotFound() {
        // Act
        Optional<User> result = userRepository.findById(UUID.randomUUID());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should save user with success")
    public void saveUserSuccess() {
        // Arrange
        User user = new User("new_user", "new@email.com", "password");

        // Act
        User savedUser = userRepository.save(user);
        
        entityManager.flush(); 

        // Assert
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getCreationTimestamp()).isNotNull();
    }
}
