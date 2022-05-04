package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.controller.form.UserForm;
import com.womakerscode.meetup.controller.form.UserUpdate;
import com.womakerscode.meetup.model.dto.UserDTO;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registrations")
public class UserController {

    private UserService service;
    private ModelMapper modelMapper;

    public UserController(UserService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid UserForm form){
        User user = modelMapper.map(form, User.class);
        user = service.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable Long id ){
        return service
                .getById(id)
                .map( user -> modelMapper.map(user, UserDTO.class)  )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id){
        User user = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        service.deleteUserById(user);
    }

    @PutMapping("{id}")
    public UserDTO updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdate update){
        return service.getById(id).map( user -> {

            user.setName(update.getName());
            user.setEmail(update.getEmail());
            user.setPassword(update.getPassword());
            user = service.updateUserById(user);
            return modelMapper.map(user, UserDTO.class);

        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @GetMapping
    public Page<UserDTO> findAllUsers(UserDTO dto, Pageable pageRequest ){
        User filter = modelMapper.map(dto, User.class);
        Page<User> result = service.findAllUser(filter, pageRequest);
        List<UserDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, UserDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<UserDTO>( list, pageRequest, result.getTotalElements() );
    }


//    @GetMapping("{id}/meetups")
//    public Page<MeetupDTO> meetupsByUser(@PathVariable Long id, Pageable pageable ){
//        User user = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        Page<Meetup> result = meetupService.getMeetupByUser(user, pageable);
//        List<MeetupDTO> list = result.getContent()
//                .stream()
//                .map(meetup -> {
//                    User meetupUser = meetup.getUser();
//                    UserDTO userDTO = modelMapper.map(meetupUser, UserDTO.class);
//                    MeetupDTO meetupDTO = modelMapper.map(meetup, MeetupDTO.class);
//                    meetupDTO.setUser(userDTO);
//                    return meetupDTO;
//                }).collect(Collectors.toList());
//        return new PageImpl<MeetupDTO>(list, pageable, result.getTotalElements());
//    }

}
