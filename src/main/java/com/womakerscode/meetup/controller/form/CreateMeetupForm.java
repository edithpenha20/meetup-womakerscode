package com.womakerscode.meetup.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMeetupForm {

    @NotEmpty
    private String event;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate eventDate;

    @NotEmpty
    private String hostedBy;

    @NotEmpty
    private String speaker;

    @NotEmpty
    private String linkMeetup;
}
