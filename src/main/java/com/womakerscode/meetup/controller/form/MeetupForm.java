package com.womakerscode.meetup.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupForm {

    @NotNull
    private String login;

    @NotNull
    private  String event;
}
