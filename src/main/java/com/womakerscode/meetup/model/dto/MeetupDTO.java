package com.womakerscode.meetup.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDTO {


    private Long id;

    private String event;

    private LocalDate meetupDateRegistration;

    //private CreateMeetupDTO eventDetails;

    private UserDTO user;

}
