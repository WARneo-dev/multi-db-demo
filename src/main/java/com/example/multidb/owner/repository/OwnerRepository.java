package com.example.multidb.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multidb.owner.model.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
