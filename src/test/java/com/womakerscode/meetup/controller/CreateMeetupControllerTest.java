package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.dto.CreateMeetupDTO;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.service.CreateMeetupService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CreateMeetupController.class)
@AutoConfigureMockMvc
public class CreateMeetupControllerTest {

    static String REQMAPPING = "/api/create-meetups";

    @Autowired
    MockMvc mvc;

    @MockBean
    CreateMeetupService service;

    @Test
    @DisplayName("Successfully register new event meetup")
    public void createEventTest() throws Exception {

        CreateMeetupDTO dto = newEventMeetupDTO();
        CreateMeetup savedEvent = CreateMeetup.builder().id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();

        BDDMockito.given(service.saveNewEventMeetup(Mockito.any(CreateMeetup.class))).willReturn(savedEvent);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REQMAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").value(1l) )
                .andExpect( jsonPath("event").value(dto.getEvent()) )
                .andExpect( jsonPath("linkMeetup").value(dto.getLinkMeetup()) )
                .andExpect( jsonPath("speaker").value(dto.getSpeaker()) )
                .andExpect( jsonPath("hostedBy").value(dto.getHostedBy()) )

        ;

    }

    @Test
    @DisplayName("Create Registration with variable registration duplicated")
    public void createEventMeetupWithVariableRegistrationDuplicated() throws Exception {

        CreateMeetupDTO dto = newEventMeetupDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given(service.saveNewEventMeetup(Mockito.any(CreateMeetup.class)))
                .willThrow(new BusinessException("Record already registered"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REQMAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform( request )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Record already registered"));

    }
//
//    @Test
//    @DisplayName("Details Event Created")
//    public void getEventMeetupTest() throws Exception{
//
//        Long id = 1L;
//
//        CreateMeetupDTO dto = newEventMeetupDTO();
//
//        CreateMeetup event = CreateMeetup.builder().id(1L)
//                .event("Microservice")
//                .linkMeetup("https://www.youtube.com/")
//                .eventDate(LocalDate.now())
//                .hostedBy("Edith")
//                .speaker("Michele Brito")
//                .build();
//
//
//        BDDMockito.given( service.getEventById(id) ).willReturn(Optional.of(event));
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .get(REQMAPPING.concat("/" + id))
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc
//                .perform(request)
//                .andExpect(status().isOk())
//                .andExpect( status().isCreated() )
//                .andExpect( jsonPath("id").value(1l) )
//                .andExpect( jsonPath("event").value(dto.getEvent()) )
//                .andExpect( jsonPath("linkMeetup").value(dto.getLinkMeetup()) )
//                .andExpect( jsonPath("speaker").value(dto.getSpeaker()) )
//                .andExpect( jsonPath("hostedBy").value(dto.getHostedBy()) )
//        ;
//    }
//
//    @Test
//    @DisplayName("Registration not found.")
//    public void registrationNotFoundTest() throws Exception {
//
//        BDDMockito.given( service.getById(anyLong()) ).willReturn( Optional.empty() );
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .get(REQMAPPING.concat("/" + 1))
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc
//                .perform(request)
//                .andExpect(status().isNotFound());
//    }
//
    @Test
    @DisplayName("Delete registration")
    public void deleteRegistrationTest() throws Exception {

        BDDMockito.given( service.getEventById(anyLong()) ).willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(REQMAPPING.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    //
    @Test
    @DisplayName("Delete registration not exist")
    public void deleteRegistrationNotExistTest() throws Exception {

        BDDMockito.given(service.getEventById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(REQMAPPING.concat("/" + 1));

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }
//
    @Test
    @DisplayName("Update event meetup")
    public void updateEventMeetupTest() throws Exception {

        Long id = 1l;
        CreateMeetupDTO dto = newEventMeetupDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        CreateMeetup updatingEvent = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michellee Brito")
                .build();

        BDDMockito.given( service.getEventById(id) ).willReturn( Optional.of(updatingEvent) );
        CreateMeetup updatedRegistration = CreateMeetup.builder().id(1L).event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();
        BDDMockito.given(service.updateEventMeetup(updatingEvent)).willReturn(updatedRegistration);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(REQMAPPING.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect(status().isOk())
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("event").value(dto.getEvent()) )
                .andExpect( jsonPath("linkMeetup").value(dto.getLinkMeetup()) )
                .andExpect( jsonPath("speaker").value(dto.getSpeaker()) )
                .andExpect( jsonPath("hostedBy").value(dto.getHostedBy()) )
        ;

    }
//
    @Test
    @DisplayName("Update event meetup Not exist.")
    public void updateEventMeetupNotExistTest() throws Exception {

        CreateMeetupDTO dto = newEventMeetupDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( service.getEventById(Mockito.anyLong()) )
                .willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(REQMAPPING.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }
//
    @Test
    @DisplayName("Filter Registration")
    public void findRegistrationTest() throws Exception{

        Long id = 1l;

        CreateMeetup eventMeetup = CreateMeetup.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michellee Brito")
                .build();

        BDDMockito.given( service.findAllEventMeetup(Mockito.any(CreateMeetup.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<CreateMeetup>( Arrays.asList(eventMeetup), PageRequest.of(0,100), 1 )   );

        String queryString = String.format("?event=%s&speaker=%s&page=0&size=100",
                eventMeetup.getEvent(), eventMeetup.getSpeaker());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(REQMAPPING.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("content", Matchers.hasSize(1)))
                .andExpect( jsonPath("totalElements").value(1) )
                .andExpect( jsonPath("pageable.pageSize").value(100) )
                .andExpect( jsonPath("pageable.pageNumber").value(0))
        ;
    }
//
    private CreateMeetupDTO newEventMeetupDTO(){
        return CreateMeetupDTO.builder()
                .id(1L)
                .event("Microservice")
                .linkMeetup("https://www.youtube.com/")
                .eventDate(LocalDate.now())
                .hostedBy("Edith")
                .speaker("Michele Brito")
                .build();
    }
}
