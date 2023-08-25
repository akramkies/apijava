package com.quest.etna.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.DetailsCommand;
import com.quest.etna.repository.DetailsCommandRepository;

@Service
public class DetailsCommandService {
    @Autowired
    private DetailsCommandRepository detailsCommandRepository;

    public Optional<DetailsCommand> getDetailsCommand(int id) {
        return detailsCommandRepository.findDetailsCommandById(id);
    }

    public Iterable<DetailsCommand> getDetailsCommands() {
        return detailsCommandRepository.findAll();
    }

    public Iterable<DetailsCommand> getDetailsCommandsByCommand(String numCommand) {
        return detailsCommandRepository.findDetailsCommandByCommand(numCommand);
    }

    public boolean deleteDetailsCommand(final int id) {
        Optional<DetailsCommand> _command = detailsCommandRepository.findDetailsCommandById(id);
        if(!_command.isEmpty()){
            detailsCommandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public DetailsCommand saveCommand(DetailsCommand command) {
        DetailsCommand savedDetailsCommand = detailsCommandRepository.save(command);
        return savedDetailsCommand;
    }
}
