package com.example.multidb.combined.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.multidb.combined.dto.OwnerWithEmployeesDTO;
import com.example.multidb.employee.model.Employee;
import com.example.multidb.employee.repository.EmployeeRepository;
import com.example.multidb.owner.model.Owner;
import com.example.multidb.owner.repository.OwnerRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Because Owner (secondary DB) and Employee (primary DB) live in two
 * completely separate databases, they cannot be joined with a single SQL
 * query / JPA @OneToMany. Instead we:
 *   1. fetch the owner(s) from the owner datasource
 *   2. fetch the matching employees from the employee datasource
 *      (matched on Employee.ownerId == Owner.id)
 *   3. stitch the two results together here, in the service layer
 */
@Service
public class OwnerEmployeeCombinedService {

    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;

    public OwnerEmployeeCombinedService(OwnerRepository ownerRepository, EmployeeRepository employeeRepository) {
        this.ownerRepository = ownerRepository;
        this.employeeRepository = employeeRepository;
    }

    /** Single owner + their employees */
    public OwnerWithEmployeesDTO getOwnerWithEmployees(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));

        List<Employee> employees = employeeRepository.findByOwnerId(ownerId);
        return new OwnerWithEmployeesDTO(owner, employees);
    }

    /** Every owner, each with their own list of employees (2 queries total, no N+1) */
    public List<OwnerWithEmployeesDTO> getAllOwnersWithEmployees() {
        List<Owner> owners = ownerRepository.findAll();

        List<Long> ownerIds = owners.stream().map(Owner::getId).collect(Collectors.toList());
        List<Employee> allEmployees = employeeRepository.findByOwnerIdIn(ownerIds);

        Map<Long, List<Employee>> employeesByOwnerId = allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getOwnerId));

        return owners.stream()
                .map(owner -> new OwnerWithEmployeesDTO(
                        owner,
                        employeesByOwnerId.getOrDefault(owner.getId(), List.of())))
                .collect(Collectors.toList());
    }
}
