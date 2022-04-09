package com.womakerscode.meetup.controller.exception;

import com.womakerscode.meetup.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetupErrors {
    private final List<String> errors;

    public MeetupErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public MeetupErrors(BusinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public MeetupErrors(ResponseStatusException ex){
        this.errors = Arrays.asList(ex.getReason());
    }

    public List<String> getErrors(){
        return errors;
    }
}
