package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private RegistrationRepository repository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Registration save(Registration registration) {
        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getById(Long id) {
        return this.repository.findById(id);
    }
}
