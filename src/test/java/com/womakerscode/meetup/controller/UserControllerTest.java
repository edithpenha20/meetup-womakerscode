package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.controller.form.UserForm;
import com.womakerscode.meetup.controller.form.UserUpdate;
import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.dto.UserDTO;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.service.UserService;
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
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    static String REQMAPPING = "/registrations";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @Test
    @DisplayName("Successfully register new user")
    public void createRegisterTest() throws Exception {

        UserDTO dto = newRegisterDTO();
        UserForm form = newRegisterForm();
        User user = User.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy")
                .build();

        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(user);
        String json = new ObjectMapper().writeValueAsString(form);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REQMAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").value(1L) )
                .andExpect( jsonPath("name").value(dto.getName()) )
                .andExpect( jsonPath("email").value(dto.getEmail()) )
//                .andExpect( jsonPath("password").value(dto.getPassword()) )
//                .andExpect( jsonPath("dateOfRegistration").value(dto.getDateOfRegistration()) )
                .andExpect( jsonPath("login").value(dto.getLogin()) )

        ;

    }

    @Test
    @DisplayName("Create Registration with variable registration duplicated")
    public void createRegistrationWithVariableRegistrationDuplicated() throws Exception {

        UserDTO dto = newRegisterDTO();
        UserForm form = newRegisterForm();
        String json = new ObjectMapper().writeValueAsString(form);
        BDDMockito.given(service.save(Mockito.any(User.class)))
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

    @Test
    @DisplayName("Details registration")
    public void getRegistrationTest() throws Exception{

        Long id = 1L;

        UserDTO dto = newRegisterDTO();

        User user = User.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy").build();


        BDDMockito.given( service.getById(id) ).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(REQMAPPING.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(dto.getName()) )
                .andExpect( jsonPath("email").value(dto.getEmail()) )
//                .andExpect( jsonPath("password").value(dto.getPassword()) )
//                .andExpect( jsonPath("dateOfRegistration").value(dto.getDateOfRegistration()) )
                .andExpect( jsonPath("login").value(dto.getLogin()) )
        ;
    }

    @Test
    @DisplayName("Registration not found.")
    public void registrationNotFoundTest() throws Exception {

        BDDMockito.given( service.getById(anyLong()) ).willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(REQMAPPING.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete registration")
    public void deleteRegistrationTest() throws Exception {

        BDDMockito.given( service.getById(anyLong()) ).willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(REQMAPPING.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete registration not exist")
    public void deleteRegistrationNotExistTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(REQMAPPING.concat("/" + 1));

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Update register")
    public void updateRegisterTest() throws Exception {

        Long id = 1l;
        UserDTO dto = newRegisterDTO();
        UserUpdate update = newRegisterUpdate();
        String json = new ObjectMapper().writeValueAsString(update);

        User updatingUser = User.builder().id(1L).name("Endy").email("endy@email.com").password("1234").dateOfRegistration(LocalDate.now()).login("endy").build();
        BDDMockito.given( service.getById(id) ).willReturn( Optional.of(updatingUser) );
        User updatedUser = User.builder().id(1L).name("Endy").email("endy@email.com").password("4321").dateOfRegistration(LocalDate.now()).login("endy").build();
        BDDMockito.given(service.updateUserById(updatingUser)).willReturn(updatedUser);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(REQMAPPING.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect(status().isOk())
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(dto.getName()) )
                .andExpect( jsonPath("email").value(dto.getEmail()) )
//                .andExpect( jsonPath("password").value(updatedRegistration.getPassword()) )
//                .andExpect( jsonPath("dateOfRegistration").value(dto.getDateOfRegistration()) )
                .andExpect( jsonPath("login").value(dto.getLogin()) );

    }

    @Test
    @DisplayName("Update Registration Not exist.")
    public void updateRegistrationNotExistTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(newRegisterUpdate());
        BDDMockito.given( service.getById(Mockito.anyLong()) )
                .willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(REQMAPPING.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Filter Registration")
    public void findRegistrationTest() throws Exception{

        Long id = 1l;

        User user = User.builder()
                .id(1L)
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("001").build();

        BDDMockito.given( service.findAllUser(Mockito.any(User.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<User>( Arrays.asList(user), PageRequest.of(0,100), 1 )   );

        String queryString = String.format("?name=%s&email=%s&page=0&size=100",
                user.getName(), user.getEmail());

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

    private UserDTO newRegisterDTO(){
        return UserDTO.builder()
                .name("Endy")
                .email("endy@email.com")
                //.password("1234")
                .dateOfRegistration(LocalDate.now())
                .login("endy")
                .build();
    }

    private UserForm newRegisterForm(){
        return UserForm.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .login("endy")
                .build();
    }

    private UserUpdate newRegisterUpdate(){
        return UserUpdate.builder()
                .name("Endy")
                .email("endy@email.com")
                .password("1234")
                .build();
    }
}
