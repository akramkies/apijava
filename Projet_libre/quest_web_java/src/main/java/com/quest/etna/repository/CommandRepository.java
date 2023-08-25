package com.quest.etna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Command;

@Repository
public interface CommandRepository extends CrudRepository<Command, Integer> {

    @Query("SELECT c from Command c WHERE numCommand like :id")
    public Optional<Command> findCommandById(Integer id);
}
