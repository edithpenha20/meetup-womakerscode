package com.womakerscode.meetup.service;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.dto.UserDTO;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.UserRepository;
import com.womakerscode.meetup.service.impl.UserServiceImpl;
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
public class UserServiceTest {

    UserService service;

    @MockBean
    UserRepository repository;


    @BeforeEach
    public void setup(){
        //dependency service

        this.service = new UserServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveStudent(){

        User user = createValidResgitration();

        when(repository.existsByLogin(Mockito.anyString())).thenReturn(false);
        when(repository.save(user)).thenReturn(createValidResgitration());

        User savedUser = service.save(user);

        assertThat(savedUser.getId()).isEqualTo(101L);
        assertThat(savedUser.getName()).isEqualTo("Endy");
        assertThat(savedUser.getLogin()).isEqualTo("001");
        assertThat(savedUser.getDateOfRegistration()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Should Not Saved As Registration Duplicated")
    public void shouldNotSavedAsRegistrationDuplicated(){

        User user = createValidResgitration();

        when(repository.existsByLogin(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(user));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("O usuário já existe.");

        Mockito.verify(repository, Mockito.never()).save(user);
    }

    @Test
    @DisplayName("Get registration by id.")
    public void getRegistrationByIdTest(){

        User user = createValidResgitration();

        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> foundRegistration = service.getById(user.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(101L);
        assertThat(foundRegistration.get().getName()).isEqualTo("Endy");
        assertThat(foundRegistration.get().getLogin()).isEqualTo("001");
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("When a record does not exist, it should return empty.")
    public void registrationByIdNotFoundTest(){
        Long id = 101L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<User> registration = service.getById(id);

        assertThat(registration.isPresent()).isFalse();
    }

    //deletar registro
    @Test
    @DisplayName("Delete registration")
    public void deleteRegistrationTest(){
        User user = createValidResgitration();

        service.deleteUserById(user);

        //Mockito.verify vai verificar se determinado comportamento aconteceu
        Mockito.verify(repository, Mockito.times(1)).delete(user);
    }

    //deletar registro não encontrado - lancar exception
    @Test
    @DisplayName("Error deleting a non-existent record")
    public void deleteRegistrationNotFound(){
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> service.deleteUserById(user));

        Mockito.verify(repository, Mockito.never()).delete(user);
    }

    //atualizar registro
    @Test
    @DisplayName("Update registration")
    public void updateRegistrationTest(){

        long id = 1L;

        User modifyingResgistration = User.builder().id(id).build();

        User modifiedUser = createValidResgitration();
        modifiedUser.setId(id);
        when(repository.save(modifyingResgistration)).thenReturn(modifiedUser);

        User user = service.updateUserById(modifyingResgistration);

        assertThat(user.getId()).isEqualTo(modifiedUser.getId());
        assertThat(user.getName()).isEqualTo(modifiedUser.getName());
        assertThat(user.getLogin()).isEqualTo(modifiedUser.getLogin());
        assertThat(user.getDateOfRegistration()).isEqualTo(modifiedUser.getDateOfRegistration());

    }

    //lancar exception ao tentar atualizar um registro inexistente
    @Test
    @DisplayName("Exception when trying to update a non-existent record")
    public void updateRegistrationNotFound(){
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> service.updateUserById(user));

        Mockito.verify(repository, Mockito.never()).save(user);
    }

    @Test
    @DisplayName("")
    public void findRegistrationTest(){

        User user = createValidResgitration();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<User> listUser = Arrays.asList(user);
        Page<User> page = new PageImpl<User>(listUser, pageRequest, 1);
        when( repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<User> result = service.findAllUser(user, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listUser);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get an registration model by registration attribute")
    public void getRegistrationByRegistrationTest(){
        String registrationAttribute = "1234";
        when(repository.findByLogin(registrationAttribute)).thenReturn( Optional.of(User.builder().id(1l).login(registrationAttribute).build()) );

        Optional<User> registration = service.getUserByLogin(registrationAttribute);

        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(1l);
        assertThat(registration.get().getLogin()).isEqualTo(registrationAttribute);

        Mockito.verify(repository, Mockito.times(1)).findByLogin(registrationAttribute);
    }

    private User createValidResgitration(){
        return User.builder()
                .id(101L)
                .name("Endy")
                .dateOfRegistration(LocalDate.now())
                .login("001")
                .build();
    }
}
