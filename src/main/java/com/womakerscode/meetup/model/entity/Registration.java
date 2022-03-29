package com.womakerscode.meetup.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resgitration_id")
    private Long id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "date_of_registration")
    private LocalDate dateOfRegistration;

    @Column
    private String registration;
}
