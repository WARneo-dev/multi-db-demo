package com.example.multidb.combined.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.multidb.combined.dto.OwnerWithEmployeesDTO;
import com.example.multidb.combined.service.OwnerEmployeeCombinedService;

@RestController
@RequestMapping("/api/owners-with-employees")
public class OwnerEmployeeCombinedController {

    private final OwnerEmployeeCombinedService combinedService;

    public OwnerEmployeeCombinedController(OwnerEmployeeCombinedService combinedService) {
        this.combinedService = combinedService;
    }

    /** GET /api/owners-with-employees -> every owner with their employees */
    @GetMapping
    public List<OwnerWithEmployeesDTO> getAll() {
        return combinedService.getAllOwnersWithEmployees();
    }

    /** GET /api/owners-with-employees/{ownerId} -> one owner with their employees */
    @GetMapping("/{ownerId}")
    public OwnerWithEmployeesDTO getOne(@PathVariable Long ownerId) {
        return combinedService.getOwnerWithEmployees(ownerId);
    }
}
