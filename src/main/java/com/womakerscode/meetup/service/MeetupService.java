package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.dto.MeetupFilter;
import com.womakerscode.meetup.model.dto.MeetupUserDTO;
import com.womakerscode.meetup.model.entity.CreateMeetup;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.controller.form.MeetupForm;
import com.womakerscode.meetup.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetupService {

    Meetup save(Meetup meetup);

    Optional<Meetup> getById(Long id);

    Meetup update(Meetup meetup);

    Page<Meetup> findAllMeetups(MeetupFilter filter, Pageable pageable);

    Page<Meetup> getUserByMeetup(User user, Pageable pageable);

}
