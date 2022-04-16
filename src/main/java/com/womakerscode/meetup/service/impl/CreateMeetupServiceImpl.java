package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.repository.CreateMeetupRepository;
import com.womakerscode.meetup.service.CreateMeetupService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateMeetupServiceImpl implements CreateMeetupService {

    private CreateMeetupRepository repository;

    public CreateMeetupServiceImpl(CreateMeetupRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreateMeetup saveNewEventMeetup(CreateMeetup createEvent) {
        return repository.save(createEvent);
    }

    @Override
    public Optional<CreateMeetup> findByEvent(String event) {
        return repository.findByEvent(event);
    }

    @Override
    public Optional<CreateMeetup> getEventById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CreateMeetup updateEventMeetup(CreateMeetup event) {
        return repository.save(event);
    }

    @Override
    public void deleteEventMeetup(CreateMeetup eventMeetup) {
        repository.delete(eventMeetup);
    }

    @Override
    public Page<CreateMeetup> findAllEventMeetup(CreateMeetup filter, Pageable pageRequest) {
        Example<CreateMeetup> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING )
        ) ;
        return repository.findAll(example, pageRequest);
    }
}
