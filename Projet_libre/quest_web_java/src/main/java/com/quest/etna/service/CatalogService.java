package com.quest.etna.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.Catalog;
import com.quest.etna.repository.CatalogRepository;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public Optional<Catalog> getCatalog(String idCatalog) {
        return catalogRepository.findCatalogById(idCatalog);
    }

    public Iterable<Catalog> getCatalogs() {
        return catalogRepository.findAll();
    }

    public boolean deleteAddress(final String id) {
        Optional<Catalog> _address = catalogRepository.findCatalogById(id);
        if(!_address.isEmpty()){
            catalogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Catalog saveAddress(Catalog catalog) {
        System.out.println(catalog);
        Catalog savedCatalog = catalogRepository.save(catalog);
        return savedCatalog;
    }
}
