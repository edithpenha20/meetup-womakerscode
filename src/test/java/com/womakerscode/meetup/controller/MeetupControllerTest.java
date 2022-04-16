package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.controller.form.MeetupFilterDTO;
import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.dto.CreateMeetupDTO;
import com.womakerscode.meetup.model.dto.RegistrationDTO;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.CreateMeetupService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static final String MEETUP_API = "/api/meetups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private CreateMeetupService createMeetupService;

    @MockBean
    private MeetupService meetupService;

    @Test
    @DisplayName("Should register on a meetup")
    public void createMeetupTest() throws Exception {

        //MeetupDTO dto = MeetupDTO.builder().eventDetails(newEventMeetupDTO()).registration(newRegisterDTO()).build();
        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Microservice").registration("endy").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("endy")
                .build();

        CreateMeetup eventMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        BDDMockito.given(registrationService.getByRegistration(registration.getRegistration())).
                willReturn(Optional.of(registration));

        BDDMockito.given(createMeetupService.findByEvent(eventMeetup.getEvent())).
                willReturn(Optional.of(eventMeetup));

        Meetup meetup = Meetup.builder().id(1L).registration(registration).eventDetails(eventMeetup).build();

        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                //.andExpect(content().string("1"))
        ;

    }
//
//
    @Test
    @DisplayName("Should return error when try to register an a meetup nonexistent")
    public void invalidRegistrationCreateMeetupTest() throws Exception {

        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Microservice").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("endy")
                .build();

        CreateMeetup eventMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        BDDMockito.given(registrationService.getByRegistration(registration.getRegistration())).
                willReturn(Optional.of(registration));

        BDDMockito.given(createMeetupService.findByEvent(eventMeetup.getEvent())).
                willReturn(Optional.of(eventMeetup));

        Meetup meetup = Meetup.builder().id(1L).registration(registration).eventDetails(eventMeetup).build();

        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }
//
//
//
    @Test
    @DisplayName("Should return error when try to register a registration already register on a meetup")
    public void  meetupRegistrationErrorOnCreateMeetupTest() throws Exception {

        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Microservice").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("endy")
                .build();

        CreateMeetup eventMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        BDDMockito.given(registrationService.getByRegistration(registration.getRegistration())).
                willReturn(Optional.of(registration));

        BDDMockito.given(createMeetupService.findByEvent(eventMeetup.getEvent())).
                willReturn(Optional.of(eventMeetup));

        Meetup meetup = Meetup.builder().id(1L).registration(registration).eventDetails(eventMeetup).build();

        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);

        // procura na base se ja tem algum registration pra esse meetup
        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willThrow(new BusinessException("Meetup already enrolled"));


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }


    private CreateMeetupDTO newEventMeetupDTO(){
        return CreateMeetupDTO.builder()
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();
    }

    private RegistrationDTO newRegisterDTO(){
        return RegistrationDTO.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .registration("001")
                .build();
    }
}
