package com.employe.service;

import com.employe.model.Employee;
import com.employe.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    public void initDefaultData() {
        try {
            long count = employeeRepository.count();
            log.info("Checking database: currently found {} employees.", count);
            if (count == 0) {
                log.info("Database is empty. Seeding initial sample employee records...");
                employeeRepository.save(new Employee(null, "John", "Doe", "john.doe@example.com", "Engineering", "Software Engineer", 85000.0));
                employeeRepository.save(new Employee(null, "Jane", "Smith", "jane.smith@example.com", "Marketing", "Marketing Manager", 90000.0));
                employeeRepository.save(new Employee(null, "Alex", "Johnson", "alex.j@example.com", "Engineering", "DevOps Engineer", 95000.0));
                employeeRepository.save(new Employee(null, "Sara", "Connor", "sara.c@example.com", "Human Resources", "HR Specialist", 70000.0));
                log.info("Initial sample employees seeded successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not seed initial data on startup (database might still be waking up or credentials need check): {}", e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees from database...");
        List<Employee> list = employeeRepository.findAll();
        log.info("Found {} employees.", list.size());
        return list;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        log.info("Finding employee by ID: {}", id);
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        log.info("Creating new employee: {} {}", employee.getFirstName(), employee.getLastName());
        employee.setId(null);
        Employee saved = employeeRepository.save(employee);
        log.info("Created employee with ID: {}", saved.getId());
        return saved;
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        log.info("Updating employee with ID: {}", id);
        return employeeRepository.findById(id).map(existing -> {
            existing.setFirstName(updatedEmployee.getFirstName());
            existing.setLastName(updatedEmployee.getLastName());
            existing.setEmail(updatedEmployee.getEmail());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setRole(updatedEmployee.getRole());
            existing.setSalary(updatedEmployee.getSalary());
            Employee saved = employeeRepository.save(existing);
            log.info("Successfully updated employee with ID: {}", saved.getId());
            return saved;
        });
    }

    public boolean deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            log.info("Employee with ID {} deleted successfully.", id);
            return true;
        }
        log.warn("Employee with ID {} does not exist. Delete skipped.", id);
        return false;
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        log.info("Filtering employees by department: {}", department);
        List<Employee> list = employeeRepository.findByDepartmentIgnoreCase(department);
        log.info("Found {} employees in department '{}'.", list.size(), department);
        return list;
    }
}
