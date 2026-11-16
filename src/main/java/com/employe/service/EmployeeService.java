package com.employe.service;

import com.employe.model.Employee;
import com.employe.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    public void initDefaultData() {
        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee(null, "John", "Doe", "john.doe@example.com", "Engineering", "Software Engineer", 85000.0));
            employeeRepository.save(new Employee(null, "Jane", "Smith", "jane.smith@example.com", "Marketing", "Marketing Manager", 90000.0));
            employeeRepository.save(new Employee(null, "Alex", "Johnson", "alex.j@example.com", "Engineering", "DevOps Engineer", 95000.0));
            employeeRepository.save(new Employee(null, "Sara", "Connor", "sara.c@example.com", "Human Resources", "HR Specialist", 70000.0));
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(null);
        return employeeRepository.save(employee);
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(existing -> {
            existing.setFirstName(updatedEmployee.getFirstName());
            existing.setLastName(updatedEmployee.getLastName());
            existing.setEmail(updatedEmployee.getEmail());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setRole(updatedEmployee.getRole());
            existing.setSalary(updatedEmployee.getSalary());
            return employeeRepository.save(existing);
        });
    }

    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentIgnoreCase(department);
    }
}
