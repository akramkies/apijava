package com.quest.etna.controller;

import java.util.ArrayList;
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
import com.quest.etna.model.Command;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.SuccessResponse;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repository.UserRepository;
import com.quest.etna.service.CommandService;

@Controller
@RequestMapping(value = "/command")
@CrossOrigin(origins = "*")
public class CommandController {
    @Autowired
    private CommandService commandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getCommands(@RequestHeader(name="Authorization") String token) {
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        Iterable<Command> commands = commandService.getCommands();

        ArrayList<Command> _commands = new ArrayList<Command>();

        if(!existUser.isEmpty()) {
            if(UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN) {
                return new ResponseEntity<>(commands, HttpStatus.OK);
            }
            else {
                for (Command command : commands) {
                    if(command.getUserID() == existUser.get().getId()) {
                        _commands.add(command);
                    }
                }
                return new ResponseEntity<>(_commands, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error getting list command"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommand(@PathVariable("id") Integer id) {
        Optional<Command> command = commandService.getCommand(id);
        System.out.println("########################");
        System.out.println(command);
        if (!command.isEmpty()) {
            return new ResponseEntity<>(command, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("Command with id = " + id + " not found!"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> createAddress(@RequestBody Command command, @RequestHeader(name="Authorization") String token) {
        if(command == null) {
            return new ResponseEntity<>(new ErrorResponse("Command should not be null"), HttpStatus.BAD_REQUEST);
        }

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            Command updatedCommand = new Command(command.getNumCommand(), command.getTransporter(), command.getNotes(), command.isUrgent(), existUser.get().getId());
            Command _command = commandService.saveCommand(updatedCommand);
            return new ResponseEntity<>(_command, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ErrorResponse("Error creating new command"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommand(@RequestBody Command newCommand, @PathVariable Integer id, @RequestHeader(name="Authorization") String token) {
        Optional<Command> _oldCommand = commandService.getCommand(id);

        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if ((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == _oldCommand.get().getUserID())) {
                if (!_oldCommand.isEmpty()) {
                    Command _newCommand = new
                        Command(
                            _oldCommand.get().getNumCommand(),
                            newCommand.getTransporter() == null ? _oldCommand.get().getTransporter() : newCommand.getTransporter(),
                            newCommand.getNotes() == null ? _oldCommand.get().getNotes() : newCommand.getNotes(),
                            newCommand.isUrgent() == null ? _oldCommand.get().isUrgent() : newCommand.isUrgent(),
                            _oldCommand.get().getUserID());
                    _newCommand.setNumCommand(id);
                    commandService.saveCommand(_newCommand);
                    return new ResponseEntity<>(_newCommand, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorResponse("Command with id" + id + " not found!"), HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("You don't have permission to update command with id = " + id), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Error updating command with id = " + id), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommand(@PathVariable Integer id, @RequestHeader(name="Authorization") String token) {
        Optional<Command> _command = commandService.getCommand(id);
        String _token = token.split(" ")[1];
        String username = "";

        username = jwtTokenUtil.getUsernameFromToken(_token);
        Optional<User> existUser = userRepository.findByUsername(username);

        if (!existUser.isEmpty()) {
            if ((UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_ADMIN)
            || (UserRole.valueOf(existUser.get().getRole()) == UserRole.ROLE_USER && existUser.get().getId() == _command.get().getUserID())) {
                if (commandService.deleteCommand(id)) {
                    return new ResponseEntity<>(new SuccessResponse(true), HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new SuccessResponse(false), HttpStatus.BAD_REQUEST);
    }
}
