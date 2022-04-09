package com.womakerscode.meetup.service;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ResgitrationServiceTest {

    RegistrationService service;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach
    public void setup(){
        //dependency service

        this.service = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveStudent(){

        Registration registration = createValidResgitration();

        when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
        when(repository.save(registration)).thenReturn(createValidResgitration());

        Registration savedRegistration = service.save(registration);

        assertThat(savedRegistration.getId()).isEqualTo(101L);
        assertThat(savedRegistration.getName()).isEqualTo("Endy");
        assertThat(savedRegistration.getRegistration()).isEqualTo("001");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Should Not Saved As Registration Duplicated")
    public void shouldNotSavedAsRegistrationDuplicated(){

        Registration registration = createValidResgitration();

        when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(registration));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("O cadastro já existe");

        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Get registration by id.")
    public void getRegistrationByIdTest(){

        Registration registration = createValidResgitration();

        when(repository.findById(registration.getId())).thenReturn(Optional.of(registration));

        Optional<Registration> foundRegistration = service.getById(registration.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(101L);
        assertThat(foundRegistration.get().getName()).isEqualTo("Endy");
        assertThat(foundRegistration.get().getRegistration()).isEqualTo("001");
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("When a record does not exist, it should return empty.")
    public void registrationByIdNotFoundTest(){
        Long id = 101L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Registration> registration = service.getById(id);

        assertThat(registration.isPresent()).isFalse();
    }

    //deletar registro
    @Test
    @DisplayName("Delete registration")
    public void deleteRegistrationTest(){
        Registration registration = createValidResgitration();

        service.deleteRegistration(registration);

        //Mockito.verify vai verificar se determinado comportamento aconteceu
        Mockito.verify(repository, Mockito.times(1)).delete(registration);
    }

    //deletar registro não encontrado - lancar exception
    @Test
    @DisplayName("Error deleting a non-existent record")
    public void deleteRegistrationNotFound(){
        Registration registration = new Registration();

        assertThrows(IllegalArgumentException.class, () -> service.deleteRegistration(registration));

        Mockito.verify(repository, Mockito.never()).delete(registration);
    }

    //atualizar registro
    @Test
    @DisplayName("Update registration")
    public void updateRegistrationTest(){

        long id = 1L;

        Registration modifyingResgistration = Registration.builder().id(id).build();

        Registration modifiedRegistration = createValidResgitration();
        modifiedRegistration.setId(id);
        when(repository.save(modifyingResgistration)).thenReturn(modifiedRegistration);

        Registration registration = service.updateRegistration(modifyingResgistration);

        assertThat(registration.getId()).isEqualTo(modifiedRegistration.getId());
        assertThat(registration.getName()).isEqualTo(modifiedRegistration.getName());
        assertThat(registration.getRegistration()).isEqualTo(modifiedRegistration.getRegistration());
        assertThat(registration.getDateOfRegistration()).isEqualTo(modifiedRegistration.getDateOfRegistration());

    }

    //lancar exception ao tentar atualizar um registro inexistente
    @Test
    @DisplayName("Exception when trying to update a non-existent record")
    public void updateRegistrationNotFound(){
        Registration registration = new Registration();

        assertThrows(IllegalArgumentException.class, () -> service.updateRegistration(registration));

        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("")
    public void findRegistrationTest(){

        Registration registration = createValidResgitration();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Registration> listRegistration = Arrays.asList(registration);
        Page<Registration> page = new PageImpl<Registration>(listRegistration, pageRequest, 1);
        when( repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Registration> result = service.findRegistration(registration, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistration);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get an registration model by registration attribute")
    public void getRegistrationByRegistrationTest(){
        String registrationAttribute = "1234";
        when(repository.findByRegistration(registrationAttribute)).thenReturn( Optional.of(Registration.builder().id(1l).registration(registrationAttribute).build()) );

        Optional<Registration> registration = service.getByRegistration(registrationAttribute);

        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(1l);
        assertThat(registration.get().getRegistration()).isEqualTo(registrationAttribute);

        Mockito.verify(repository, Mockito.times(1)).findByRegistration(registrationAttribute);
    }

    private Registration createValidResgitration(){
        return Registration.builder()
                .id(101L)
                .name("Endy")
                .dateOfRegistration(LocalDate.now())
                .registration("001")
                .build();
    }
}
