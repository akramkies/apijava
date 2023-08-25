package com.quest.etna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.quest.etna.model.Catalog;

public interface CatalogRepository extends CrudRepository<Catalog, String>{

    @Query("SELECT c from Catalog c WHERE reference like %:id%")
    public Optional<Catalog> findCatalogById(String id);
}
