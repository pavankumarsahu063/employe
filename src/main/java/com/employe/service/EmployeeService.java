package com.employe.service;

import com.employe.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final Map<Long, Employee> employeeMap = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public EmployeeService() {
        // Pre-populate with sample employees
        createEmployee(new Employee(null, "John", "Doe", "john.doe@example.com", "Engineering", "Software Engineer", 85000.0));
        createEmployee(new Employee(null, "Jane", "Smith", "jane.smith@example.com", "Marketing", "Marketing Manager", 90000.0));
        createEmployee(new Employee(null, "Alex", "Johnson", "alex.j@example.com", "Engineering", "DevOps Engineer", 95000.0));
        createEmployee(new Employee(null, "Sara", "Connor", "sara.c@example.com", "Human Resources", "HR Specialist", 70000.0));
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    public Employee createEmployee(Employee employee) {
        long newId = idCounter.getAndIncrement();
        employee.setId(newId);
        employeeMap.put(newId, employee);
        return employee;
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        if (!employeeMap.containsKey(id)) {
            return Optional.empty();
        }
        updatedEmployee.setId(id);
        employeeMap.put(id, updatedEmployee);
        return Optional.of(updatedEmployee);
    }

    public boolean deleteEmployee(Long id) {
        return employeeMap.remove(id) != null;
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeMap.values().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
}
