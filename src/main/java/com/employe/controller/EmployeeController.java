package com.employe.controller;

import com.employe.model.Employee;
import com.employe.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /api/employees - Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("REST request to get all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // GET /api/employees/{id} - Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee by ID: {}", id);
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Employee with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // POST /api/employees - Add a new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        log.info("REST request to create employee: {}", employee);
        Employee created = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/employees/{id} - Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        log.info("REST request to update employee with ID: {}", id);
        return employeeService.updateEmployee(id, employee)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Cannot update. Employee with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // DELETE /api/employees/{id} - Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee with ID: {}", id);
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        log.warn("Cannot delete. Employee with ID {} not found", id);
        return ResponseEntity.notFound().build();
    }

    // GET /api/employees/department/{department} - Filter by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
        log.info("REST request to get employees by department: {}", department);
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }
}
