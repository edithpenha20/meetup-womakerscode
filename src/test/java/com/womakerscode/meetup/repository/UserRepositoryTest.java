package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Return true when user exists")
    public void returnTrueWhenUserExists(){

        String login = "endy";
        User userAttribute = createNewUser(login);
        entityManager.persist(userAttribute);


        boolean exists = repository.existsByLogin(login);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Returns false if user does not exist")
    public void returnFalseWhenUserDoesntExist(){

        String registration = "endy";

        boolean isExists = repository.existsByLogin(registration);

        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("Find user by id")
    public void findUserByIdTest(){

        String login = "endy";
        User userAttribute = createNewUser(login);
        entityManager.persist(userAttribute);

        Optional<User> foundRegistration = repository.findById(userAttribute.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should save an user")
    public void saveUserTest() {

        User user_Class_attribute = createNewUser("endy");

        User savedUser = repository.save(user_Class_attribute);

        assertThat(savedUser.getId()).isNotNull();

    }

    @Test
    @DisplayName("Should delete and user from the base")
    public void deleteUser() {

        User user_Class_attribute = createNewUser("endy");
        entityManager.persist(user_Class_attribute);

        User foundUser = entityManager
                .find(User.class, user_Class_attribute.getId());
        repository.delete(foundUser);

        User deleteUser = entityManager
                .find(User.class, user_Class_attribute.getId());

        assertThat(deleteUser).isNull();

    }

    public static User createNewUser(String login) {
        return User.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy")
                .build();
    }
}
