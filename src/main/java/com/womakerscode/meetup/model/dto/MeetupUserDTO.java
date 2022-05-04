package com.womakerscode.meetup.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupUserDTO {

    private Long id;

    private Long login;

    private String event;

    private LocalDate eventDate;

    private String hostedBy;

    private String speaker;

    private String linkMeetup;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate meetupDateRegistration = LocalDate.now();

}
