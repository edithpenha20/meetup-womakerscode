package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.data.domain.*;
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
        if (repository.existsByRegistration(registration.getRegistration())){
            throw  new BusinessException("O cadastro já existe");
        }
        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteRegistration(Registration registration) {
        if (registration == null || registration.getId() == null){
            throw new IllegalArgumentException("O id do registro não pode ser nulo");
        }
        this.repository.delete(registration);
    }

    @Override
    public Registration updateRegistration(Registration registration) {
        if (registration == null || registration.getId() == null){
            throw new IllegalArgumentException("O id do registro não pode ser nulo");
        }
        return this.repository.save(registration);
    }

    @Override
    public Page<Registration> findRegistration(Registration filter, Pageable pageRequest) {
        Example<Registration> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING )
        ) ;
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Registration> getByRegistration(String registration) {
        return repository.findByRegistration(registration);
    }

    @Override
    public Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute) {
        return repository.findByRegistration(registrationAttribute);
    }
}
