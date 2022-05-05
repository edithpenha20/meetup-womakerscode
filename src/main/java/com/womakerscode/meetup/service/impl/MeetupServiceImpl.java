package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.dto.*;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.controller.form.MeetupForm;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.CreateMeetupService;
import com.womakerscode.meetup.service.MeetupService;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetupServiceImpl implements MeetupService {


    private MeetupRepository repository;

    public MeetupServiceImpl(MeetupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meetup save(Meetup meetup) {
        return repository.save(meetup);
    }

    @Override
    public Optional<Meetup> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Meetup update(Meetup meetup) {
        return repository.save(meetup);
    }

    @Override
    public Page<Meetup> findAllMeetups(MeetupFilter filter, Pageable pageable) {
        return repository.findByUserOnMeetup( filter.getLogin(), filter.getEvent(), pageable );
    }

    @Override
    public Page<Meetup> getUserByMeetup(User user, Pageable pageable) {
        return repository.findByUser(user, pageable);
    }

}
