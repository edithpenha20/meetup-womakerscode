package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.controller.form.CreateMeetupForm;
import com.womakerscode.meetup.model.dto.CreateMeetupDTO;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.service.CreateMeetupService;
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
@RequestMapping("/api/create-meetups")
public class CreateMeetupController {

    private final CreateMeetupService service;
    private final ModelMapper modelMapper;

    public CreateMeetupController(CreateMeetupService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMeetupDTO createEventMeetup(@RequestBody @Valid CreateMeetupForm form){
        CreateMeetup createEvent = modelMapper.map(form, CreateMeetup.class);
        createEvent = service.saveNewEventMeetup(createEvent);
        return modelMapper.map(createEvent, CreateMeetupDTO.class);
    }

    @PutMapping("{id}")
    public CreateMeetupDTO updateEvent(@PathVariable Long id, @RequestBody @Valid CreateMeetupForm form){
        return service.getEventById(id).map( event -> {

            event.setEvent(form.getEvent());
            event.setEventDate(form.getEventDate());
            event.setLinkMeetup(form.getLinkMeetup());
            event.setSpeaker(form.getSpeaker());
            event = service.updateEventMeetup(event);
            return modelMapper.map(event, CreateMeetupDTO.class);

        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id){
        CreateMeetup eventMeetup = service.getEventById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        service.deleteEventMeetup(eventMeetup);
    }

    @GetMapping
    public Page<CreateMeetupDTO> find(CreateMeetupForm form, Pageable pageRequest ){
        CreateMeetup filter = modelMapper.map(form, CreateMeetup.class);
        Page<CreateMeetup> result = service.findAllEventMeetup(filter, pageRequest);
        List<CreateMeetupDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, CreateMeetupDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<CreateMeetupDTO>( list, pageRequest, result.getTotalElements() );
    }

}
