package com.example.multidb.owner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.multidb.owner.model.Owner;
import com.example.multidb.owner.repository.OwnerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner create(Owner owner) {
        owner.setId(null);
        return ownerRepository.save(owner);
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public Owner findById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));
    }

    public Owner update(Long id, Owner updated) {
        Owner existing = findById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setCompanyName(updated.getCompanyName());
        existing.setPhone(updated.getPhone());
        return ownerRepository.save(existing);
    }

    public void delete(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new EntityNotFoundException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }
}
