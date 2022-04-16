package com.womakerscode.meetup.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.womakerscode.meetup.controller.form.CreateMeetupForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDTO {


    private Long id;

    //private String registrationAttribute;

    private RegistrationDTO registration;

    private CreateMeetupDTO eventDetails;
}
