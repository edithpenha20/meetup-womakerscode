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

        String registration = "001";
        Registration registrationAttribute = createNewRegistration();
        entityManager.persist(registrationAttribute);


        boolean exists = repository.existsByRegistration(registration);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Returns false if registration does not exist")
    public void returnFalseWhenRegistrationDoesntExist(){

        String registration = "001";

        boolean isExists = repository.existsByRegistration(registration);

        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("Find registration by id")
    public void findRegistrationByIdTest(){

        Registration registrationAttribute = createNewRegistration();
        entityManager.persist(registrationAttribute);

        Optional<Registration> foundRegistration = repository.findById(registrationAttribute.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
    }

    public static Registration createNewRegistration() {
        return Registration.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("001")
                .build();
    }
}
