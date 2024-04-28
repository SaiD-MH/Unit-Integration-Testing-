package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {


    Employee saveEmployee(Employee employee);

    List<Employee>getAllEmployee();

    Optional<Employee> findEmployeeById(Long id);


    Employee updateEmployee(Employee employee);


    void deleteEmployee(Long id);


}
