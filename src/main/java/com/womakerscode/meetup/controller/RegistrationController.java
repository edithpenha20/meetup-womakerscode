package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.controller.exception.MeetupErrors;
import com.womakerscode.meetup.controller.form.RegistrationForm;
import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.RegistrationDTO;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private RegistrationService service;
    private ModelMapper modelMapper;

    public RegistrationController(RegistrationService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationDTO createRegister(@RequestBody @Valid RegistrationForm form){
        Registration registration = modelMapper.map(form, Registration.class);
        registration = service.save(registration);
        return modelMapper.map(registration, RegistrationDTO.class);
    }

    @GetMapping("{id}")
    public RegistrationDTO get( @PathVariable Long id ){
        return service
                .getById(id)
                .map( registration -> modelMapper.map(registration, RegistrationDTO.class)  )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Registration registration = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        service.deleteRegistration(registration);
    }

    @PutMapping("{id}")
    public RegistrationDTO update( @PathVariable Long id, @RequestBody @Valid RegistrationForm form){
        return service.getById(id).map( reg -> {

            reg.setName(form.getName());
            reg.setEmail(form.getEmail());
            reg = service.updateRegistration(reg);
            return modelMapper.map(reg, RegistrationDTO.class);

        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @GetMapping
    public Page<RegistrationDTO> find(Registration dto, Pageable pageRequest ){
        Registration filter = modelMapper.map(dto, Registration.class);
        Page<Registration> result = service.findRegistration(filter, pageRequest);
        List<RegistrationDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, RegistrationDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<RegistrationDTO>( list, pageRequest, result.getTotalElements() );
    }

}
