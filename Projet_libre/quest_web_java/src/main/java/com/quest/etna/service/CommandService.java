package com.quest.etna.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.Command;
import com.quest.etna.repository.CommandRepository;

@Service
public class CommandService {
    @Autowired
    private CommandRepository commandRepository;

    public Optional<Command> getCommand(Integer idCommand) {
        return commandRepository.findCommandById(idCommand);
    }

    public Iterable<Command> getCommands() {
        return commandRepository.findAll();
    }

    public boolean deleteCommand(final Integer id) {
        Optional<Command> _command = commandRepository.findCommandById(id);
        if(!_command.isEmpty()){
            commandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Command saveCommand(Command catalog) {
        Command savedCommand = commandRepository.save(catalog);
        return savedCommand;
    }
}
