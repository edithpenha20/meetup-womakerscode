package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.CreateMeetup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CreateMeetupService {

    CreateMeetup saveNewEventMeetup(CreateMeetup createEvent);

    Optional<CreateMeetup> findByEvent(String event);

    Optional<CreateMeetup> getEventById(Long id);

    CreateMeetup updateEventMeetup(CreateMeetup event);

    void deleteEventMeetup(CreateMeetup eventMeetup);

    Page<CreateMeetup> findAllEventMeetup(CreateMeetup filter, Pageable pageRequest);
}
