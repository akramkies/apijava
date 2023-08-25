package com.quest.etna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Address;



@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    @Query("SELECT u from Address u WHERE id like :id")
    public Optional<Address> findAddressById(int id);

}
