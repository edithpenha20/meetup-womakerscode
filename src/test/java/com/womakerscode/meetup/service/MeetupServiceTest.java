package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    MeetupService service;

    @MockBean
    MeetupRepository repository;

    @BeforeEach
    public void setup() {
        this.service = new MeetupServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save schedule a meetup held by the user.")
    public void saveMeetupTest(){

        Meetup meetupSaved = createMeetup();

        when( repository.save(createMeetup()) ).thenReturn( meetupSaved );

        Meetup meetup = service.save(meetupSaved);

        assertThat(meetup.getId()).isEqualTo(meetupSaved.getId());
        assertThat(meetup.getMeetupDateRegistration()).isEqualTo(meetupSaved.getMeetupDateRegistration());
        assertThat(meetup.getUser()).isEqualTo(meetupSaved.getUser());
        assertThat(meetup.getEventDetails()).isEqualTo(meetupSaved.getEventDetails());
    }

    @Test
    @DisplayName("Should update schedule a meetup held by the user.")
    public void updateMeetupTest(){
        User user = User.builder()
                .id(1L)
                .name("Endy Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy")
                .build();
        CreateMeetup createMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice with Spring Boot")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        Meetup updatingMeetup = createMeetup();
        updatingMeetup.setId(1l);

        Meetup updatedMeetup = Meetup.builder()
                .meetupDateRegistration(LocalDate.now())
                .user(user)
                .eventDetails(createMeetup)
                .build();

        when( repository.save(updatedMeetup) ).thenReturn( updatedMeetup );

        Meetup meetup = service.update(updatedMeetup);

        assertThat(meetup.getId()).isEqualTo(updatedMeetup.getId());
        assertThat(meetup.getMeetupDateRegistration()).isEqualTo(updatedMeetup.getMeetupDateRegistration());
        assertThat(meetup.getUser()).isEqualTo(updatedMeetup.getUser());
        assertThat(meetup.getEventDetails()).isEqualTo(updatedMeetup.getEventDetails());
    }

    @Test
    @DisplayName("Should return a meetup when searched by id")
    public void getMeetupByIdTest(){

        Long id = 1L;

        Meetup meetup = createMeetup();

        meetup.setId(id);

        Mockito.when( repository.findById(id) ).thenReturn(Optional.of(meetup));

        Optional<Meetup> result = service.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getMeetupDateRegistration()).isEqualTo(meetup.getMeetupDateRegistration());
        assertThat(result.get().getUser()).isEqualTo(meetup.getUser());
        assertThat(result.get().getEventDetails()).isEqualTo(meetup.getEventDetails());

        verify( repository ).findById(id);

    }

    public static Meetup createMeetup(){
        Meetup meetup = Meetup.builder().id(1l).build();
        User user = User.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy")
                .build();
        CreateMeetup createMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        return Meetup.builder()
                .meetupDateRegistration(LocalDate.now())
                .user(user)
                .eventDetails(createMeetup)
                .build();
    }

}
