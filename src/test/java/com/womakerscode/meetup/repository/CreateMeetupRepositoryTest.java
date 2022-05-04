package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.CreateMeetup;
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
public class CreateMeetupRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CreateMeetupRepository repository;


    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest(){

        CreateMeetup eventMeetup = createNewEvent("Microservice");
        entityManager.persist(eventMeetup);

        Optional<CreateMeetup> foundEvent = repository.findById(eventMeetup.getId());

        assertThat(foundEvent.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveEventMeetupTest() {

        CreateMeetup newEvent = createNewEvent("Microservice");

        CreateMeetup savedRegistration = repository.save(newEvent);

        assertThat(savedRegistration.getId()).isNotNull();

    }

    @Test
    @DisplayName("Should delete and registration from the base")
    public void deleteEventMeetup() {

        CreateMeetup event = createNewEvent("Microservice");
        entityManager.persist(event);

        CreateMeetup foundEvent = entityManager
                .find(CreateMeetup.class, event.getId());
        repository.delete(foundEvent);

        User deleteUser = entityManager
                .find(User.class, event.getId());

        assertThat(deleteUser).isNull();

    }

    public static CreateMeetup createNewEvent(String event) {
        return CreateMeetup.builder().event(event).speaker("Michele Brito").eventDate(LocalDate.now()).build();
    }
}
