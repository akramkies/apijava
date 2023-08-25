package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.DetailsCommand;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.SuccessResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.DetailsCommandService;

@Controller
@RequestMapping(value = "/details")
@CrossOrigin(origins = "*")
public class DetailsCommandController {
    @Autowired
    private DetailsCommandService detailsCommandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getDetailsCommands(@RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        Iterable<DetailsCommand> commands = detailsCommandService.getDetailsCommands();

        if(!existUser.isEmpty()) {
            if(UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN) {
                return new ResponseEntity<>(commands, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("You don't have permission to access to this resources"), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error getting list details command"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailsCommand(@PathVariable("id") int id) {
        Optional<DetailsCommand> detailsCommand = detailsCommandService.getDetailsCommand(id);

        if (!detailsCommand.isEmpty()) {
            return new ResponseEntity<>(detailsCommand, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("Details Command with id = " + id + " not found!"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/command/{id}")
    public ResponseEntity<?> getDetailsCommandByCommand(@PathVariable("id") String id) {
        Iterable<DetailsCommand> detailsCommand = detailsCommandService.getDetailsCommandsByCommand(id);
        return new ResponseEntity<>(detailsCommand, HttpStatus.OK);
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> createDetailsCommand(@RequestBody DetailsCommand command, @RequestHeader(name="Authorization") String token) {
        if(command == null) {
            return new ResponseEntity<>(new ErrorResponse("Command should not be null"), HttpStatus.BAD_REQUEST);
        }

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            DetailsCommand updatedCommand = new DetailsCommand(command.getNumCommand(), command.getReference(),command.isRemise(), command.getQuantity());
            DetailsCommand _command = detailsCommandService.saveCommand(updatedCommand);
            return new ResponseEntity<>(_command, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ErrorResponse("Error creating new details command"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommand(@RequestBody DetailsCommand newCommand, @PathVariable int id, @RequestHeader(name="Authorization") String token) {
        Optional<DetailsCommand> _oldCommand = detailsCommandService.getDetailsCommand(id);

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if (!_oldCommand.isEmpty()) {
                DetailsCommand _newCommand = new
                    DetailsCommand(
                        _oldCommand.get().getNumCommand(),
                        _oldCommand.get().getReference(),
                        newCommand.isRemise() == null ? _oldCommand.get().isRemise() : newCommand.isRemise(),
                        newCommand.getQuantity() == null ? _oldCommand.get().getQuantity() : newCommand.getQuantity());
                _newCommand.setId(id);
                detailsCommandService.saveCommand(_newCommand);
                return new ResponseEntity<>(_newCommand, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("Details Command with id" + id + " not found!"), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error updating details command with id = " + id), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommand(@PathVariable int id, @RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if (detailsCommandService.deleteDetailsCommand(id)) {
                return new ResponseEntity<>(new SuccessResponse(true), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.BAD_REQUEST);
    }
}
