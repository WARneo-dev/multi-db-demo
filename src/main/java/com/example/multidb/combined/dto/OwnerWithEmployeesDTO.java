package com.example.multidb.combined.dto;

import java.util.List;

import com.example.multidb.employee.model.Employee;
import com.example.multidb.owner.model.Owner;

/**
 * Combines data pulled from TWO different databases:
 *  - Owner    -> secondary datasource (owner db)
 *  - Employees -> primary datasource (employee db), matched by Employee.ownerId
 */
public class OwnerWithEmployeesDTO {

    private Owner owner;
    private List<Employee> employees;

    public OwnerWithEmployeesDTO() {
    }

    public OwnerWithEmployeesDTO(Owner owner, List<Employee> employees) {
        this.owner = owner;
        this.employees = employees;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
