package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RegistrationService {

    Registration save(Registration any);

    Optional<Registration> getById(Long id);

    void deleteRegistration(Registration registration);

    Registration updateRegistration(Registration registration);

    Page<Registration> findRegistration(Registration registration, Pageable pageRequest);

    Optional<Registration> getByRegistration(String registrationAttribute);
}
