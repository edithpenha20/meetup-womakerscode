package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.dto.RegistrationDTO;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.controller.form.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.CreateMeetupService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetups")
public class MeetupController {

    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final CreateMeetupService createMeetupService;
    private final ModelMapper modelMapper;

    public MeetupController(MeetupService meetupService, RegistrationService registrationService, CreateMeetupService createMeetupService, ModelMapper modelMapper) {
        this.meetupService = meetupService;
        this.registrationService = registrationService;
        this.createMeetupService = createMeetupService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private MeetupDTO registerForAnEvent(@RequestBody MeetupFilterDTO filterDTO) {

        Registration registration = registrationService.getByRegistration(filterDTO.getRegistration())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        CreateMeetup newEvent = createMeetupService.findByEvent(filterDTO.getEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Meetup entity = Meetup.builder()
                .eventDetails(newEvent)
                .registration(registration)
                .build();

        entity = meetupService.save(entity);
        MeetupDTO dto = modelMapper.map(entity, MeetupDTO.class);
        return dto;
    }


    @GetMapping
    public Page<MeetupDTO> findAllMeetups(MeetupFilterDTO dto, Pageable pageRequest) {
        Page<Meetup> result = meetupService.findAllMeetups(dto, pageRequest);
        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {

                    Registration registration = entity.getRegistration();
                    RegistrationDTO registrationDTO = modelMapper.map(registration, RegistrationDTO.class);

                    MeetupDTO meetupDTO = modelMapper.map(entity, MeetupDTO.class);
                    meetupDTO.setRegistration(registrationDTO);
                    return meetupDTO;

                }).collect(Collectors.toList());
        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }
}
