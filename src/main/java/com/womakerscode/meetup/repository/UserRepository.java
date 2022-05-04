package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

}
