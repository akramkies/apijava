package com.quest.etna.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.Address;
import com.quest.etna.repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Optional<Address> getAddress(int idAddress) {
        return addressRepository.findAddressById(idAddress);
    }

    public Iterable<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public boolean deleteAddress(final int id) {
        Optional<Address> _address = addressRepository.findAddressById(id);
        if(!_address.isEmpty()){
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Address saveAddress(Address address) {
        Address savedAddress = addressRepository.save(address);
        return savedAddress;
    }
}
