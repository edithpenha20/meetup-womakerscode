package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.controller.form.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MeetupService {

    Meetup save(Meetup meetup);

    Optional<Meetup> getById(Long id);

    Meetup update(Meetup meetup);

    Page<Meetup> findAllMeetups(MeetupFilterDTO filterDTO, Pageable pageable);

    Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable);

}
