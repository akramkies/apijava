package com.quest.etna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.DetailsCommand;

@Repository
public interface DetailsCommandRepository extends CrudRepository<DetailsCommand, Integer>  {

    @Query("SELECT d from DetailsCommand d WHERE id like :id")
    public Optional<DetailsCommand> findDetailsCommandById(int id);

    @Query("SELECT d from DetailsCommand d WHERE numCommand like %:numCommand%")
    public Iterable<DetailsCommand> findDetailsCommandByCommand(String numCommand);
}
