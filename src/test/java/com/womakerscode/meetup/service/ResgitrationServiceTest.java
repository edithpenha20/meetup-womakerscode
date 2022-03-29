package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.RegistrationServiceImpl;
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

        //cenario
        Registration registration = createValidResgitration();

        //execucao
        //Mockito.when(repository.existsByResgistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidResgitration());

        Registration savedRegistration = service.save(registration);

        //assert
        assertThat(savedRegistration.getId()).isEqualTo(101L);
        assertThat(savedRegistration.getName()).isEqualTo("Endy");
        assertThat(savedRegistration.getRegistration()).isEqualTo("001");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Get registration by id.")
    public void getByIdTest(){

        //cenario
        Registration registration = createValidResgitration();

        //execucao
        //Mockito.when(repository.existsByResgistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.findById(registration.getId())).thenReturn(Optional.of(registration));

        Optional<Registration> foundRegistration = service.getById(registration.getId());

        //assert
        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(101L);
        assertThat(foundRegistration.get().getName()).isEqualTo("Endy");
        assertThat(foundRegistration.get().getRegistration()).isEqualTo("001");
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(LocalDate.now());
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
