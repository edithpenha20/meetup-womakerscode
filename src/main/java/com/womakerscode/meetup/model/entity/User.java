package com.womakerscode.meetup.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_email")
    private String email;

    @Column(name = "person_password")
    private String password;

    @Column(name = "date_of_registration")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRegistration = LocalDate.now();

    @Column
    private String login;

    @OneToMany(mappedBy = "user")
    private List<Meetup> meetups;

    @OneToMany(mappedBy = "users")
    private List<CreateMeetup> eventDetails;
}
