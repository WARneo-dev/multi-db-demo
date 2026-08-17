package com.example.multidb.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multidb.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByOwnerId(Long ownerId);

    List<Employee> findByOwnerIdIn(List<Long> ownerIds);
}
