package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.UserRepository;
import com.womakerscode.meetup.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        if (repository.existsByLogin(user.getLogin())){
            throw  new BusinessException("O usuário já existe.");
        }
        return repository.save(user);
    }

    @Override
    public Optional<User> getById(Long id) {
        return this.repository.findById(id);
    }


    @Override
    public void deleteUserById(User user) {
        if (user == null || user.getId() == null){
            throw new IllegalArgumentException("O id do usuário não pode ser nulo");
        }
        this.repository.delete(user);
    }

    @Override
    public User updateUserById(User user) {
        if (user == null || user.getId() == null){
            throw new IllegalArgumentException("O id do usuário não pode ser nulo");
        }
        return this.repository.save(user);
    }

    @Override
    public Page<User> findAllUser(User filter, Pageable pageRequest) {
        Example<User> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING )
        ) ;
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return repository.findByLogin(login);
    }

//    @Override
//    public Optional<User> getUserByLoginAttribute(String registrationAttribute) {
//        return repository.findByLogin(registrationAttribute);
//    }
}
