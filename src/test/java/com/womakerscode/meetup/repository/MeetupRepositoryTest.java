package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.womakerscode.meetup.repository.UserRepositoryTest.createNewUser;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

    @Autowired
    private MeetupRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should search for a login by meetup or event")
    public void findByMeetupLoginOrEventTest(){
        Meetup meetup = createAndPersistMeetup();

        Page<Meetup> result = repository.findByUserOnMeetup(
                "endy", "Microservice", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(meetup);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }


    public Meetup createAndPersistMeetup(){
        User user = createNewUser("endy");
        entityManager.persist(user);

        Meetup meetup = Meetup.builder().user(user).event("Microservice").build();
        entityManager.persist(meetup);

        return meetup;
    }

}
