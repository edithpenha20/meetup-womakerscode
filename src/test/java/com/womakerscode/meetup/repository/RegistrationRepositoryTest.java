package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Registration;
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
public class RegistrationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RegistrationRepository repository;

    @Test
    @DisplayName("Return true when registration exists")
    public void returnTrueWhenRegistrationExists(){

        String registration = "endy";
        Registration registrationAttribute = createNewRegistration(registration);
        entityManager.persist(registrationAttribute);


        boolean exists = repository.existsByRegistration(registration);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Returns false if registration does not exist")
    public void returnFalseWhenRegistrationDoesntExist(){

        String registration = "endy";

        boolean isExists = repository.existsByRegistration(registration);

        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("Find registration by id")
    public void findRegistrationByIdTest(){

        String registration = "endy";
        Registration registrationAttribute = createNewRegistration(registration);
        entityManager.persist(registrationAttribute);

        Optional<Registration> foundRegistration = repository.findById(registrationAttribute.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationTest() {

        Registration registration_Class_attribute = createNewRegistration("endy");

        Registration savedRegistration = repository.save(registration_Class_attribute);

        assertThat(savedRegistration.getId()).isNotNull();

    }

    @Test
    @DisplayName("Should delete and registration from the base")
    public void deleteRegistation() {

        Registration registration_Class_attribute = createNewRegistration("endy");
        entityManager.persist(registration_Class_attribute);

        Registration foundRegistration = entityManager
                .find(Registration.class, registration_Class_attribute.getId());
        repository.delete(foundRegistration);

        Registration deleteRegistration = entityManager
                .find(Registration.class, registration_Class_attribute.getId());

        assertThat(deleteRegistration).isNull();

    }

    public static Registration createNewRegistration(String registration) {
        return Registration.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("endy")
                .build();
    }
}
