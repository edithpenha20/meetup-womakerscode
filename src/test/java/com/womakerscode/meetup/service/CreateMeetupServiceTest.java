package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.repository.CreateMeetupRepository;
import com.womakerscode.meetup.service.impl.CreateMeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CreateMeetupServiceTest {

    CreateMeetupService service;

    @MockBean
    CreateMeetupRepository repository;

    @BeforeEach
    public void setup(){
        //dependency service

        this.service = new CreateMeetupServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an event meetup")
    public void saveEventMeetup(){

        CreateMeetup eventMeetup = createEventMeetupValid();

        when(repository.save(eventMeetup)).thenReturn(createEventMeetupValid());

        CreateMeetup savedRegistration = service.saveNewEventMeetup(eventMeetup);

        assertThat(savedRegistration.getId()).isEqualTo(1L);
        assertThat(savedRegistration.getEvent()).isEqualTo("Microservice");
        assertThat(savedRegistration.getHostedBy()).isEqualTo("Edith");
        assertThat(savedRegistration.getEventDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Get event meetup by id.")
    public void getEventMeetupByIdTest(){

        CreateMeetup eventMeetup = createEventMeetupValid();

        when(repository.findById(eventMeetup.getId())).thenReturn(Optional.of(eventMeetup));

        Optional<CreateMeetup> foundEvent = service.getEventById(eventMeetup.getId());

        assertThat(foundEvent.isPresent()).isTrue();
        assertThat(foundEvent.get().getId()).isEqualTo(1L);
        assertThat(foundEvent.get().getEvent()).isEqualTo("Microservice");
        assertThat(foundEvent.get().getHostedBy()).isEqualTo("Edith");
        assertThat(foundEvent.get().getEventDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("When a event meetup does not exist, it should return empty.")
    public void eventMeetupByIdNotFoundTest(){
        Long id = 101L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<CreateMeetup> eventMeetup = service.getEventById(id);

        assertThat(eventMeetup.isPresent()).isFalse();
    }

    //deletar registro
    @Test
    @DisplayName("Delete registration")
    public void deleteRegistrationTest(){

        CreateMeetup eventMeetup = createEventMeetupValid();

        service.deleteEventMeetup(eventMeetup);

        //Mockito.verify vai verificar se determinado comportamento aconteceu
        Mockito.verify(repository, Mockito.times(1)).delete(eventMeetup);
    }

    //deletar registro não encontrado - lancar exception
//    @Test
//    @DisplayName("Error deleting a non-existent record")
//    public void deleteEventMeetupNotFound(){
//
//        CreateMeetup eventMeetup = createEventMeetupValid();
//
//        assertThrows(IllegalArgumentException.class, () -> service.deleteEventMeetup(eventMeetup));
//
//        Mockito.verify(repository, Mockito.never()).delete(eventMeetup);
//    }

    //atualizar registro
    @Test
    @DisplayName("Update event meetup")
    public void updateEventMeetupTest(){

        long id = 1L;

        CreateMeetup updatingEventMeetup = CreateMeetup.builder().id(id).build();

        CreateMeetup updatedEventMeetup = createEventMeetupValid();
        updatedEventMeetup.setId(id);
        when(repository.save(updatingEventMeetup)).thenReturn(updatedEventMeetup);

        CreateMeetup eventMeetup = service.updateEventMeetup(updatingEventMeetup);

        assertThat(eventMeetup.getId()).isEqualTo(updatedEventMeetup.getId());
        assertThat(eventMeetup.getEvent()).isEqualTo(updatedEventMeetup.getEvent());
        assertThat(eventMeetup.getHostedBy()).isEqualTo(updatedEventMeetup.getHostedBy());
        assertThat(eventMeetup.getEventDate()).isEqualTo(updatedEventMeetup.getEventDate());

    }

    //lancar exception ao tentar atualizar um registro inexistente
//    @Test
//    @DisplayName("Exception when trying to update a non-existent event meetup")
//    public void updateEventMeetupNotFound(){
//        CreateMeetup eventMeetup = new CreateMeetup();
//
//        assertThrows(IllegalArgumentException.class, () -> service.updateEventMeetup(eventMeetup));
//
//        Mockito.verify(repository, Mockito.never()).save(eventMeetup);
//    }

    @Test
    @DisplayName("Should find all meetup events")
    public void findAllEventMeetupTest(){

        CreateMeetup eventMeetup = createEventMeetupValid();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<CreateMeetup> listRegistration = Arrays.asList(eventMeetup);
        Page<CreateMeetup> page = new PageImpl<CreateMeetup>(listRegistration, pageRequest, 1);
        when( repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<CreateMeetup> result = service.findAllEventMeetup(eventMeetup, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistration);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }


    private CreateMeetup createEventMeetupValid(){
        return CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();
    }
}
