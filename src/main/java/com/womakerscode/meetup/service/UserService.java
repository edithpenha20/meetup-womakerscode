package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User save(User any);

    Optional<User> getById(Long id);

    void deleteUserById(User user);

    User updateUserById(User user);

    Page<User> findAllUser(User user, Pageable pageRequest);

    Optional<User> getUserByLogin(String login);

//    Optional<User> getUserByLoginAttribute(String registrationAttribute);


//    Optional<UserMeetupDTO> getByUserAndMeetupsId(Long id);
}
