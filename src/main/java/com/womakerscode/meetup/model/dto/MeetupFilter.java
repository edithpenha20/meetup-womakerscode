package com.womakerscode.meetup.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupFilter {

    @NotNull
    private  String event;

    @NotNull
    private String login;

}
