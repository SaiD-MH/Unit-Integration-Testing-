package net.javaguides.springboottesting.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {


    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){

        return employeeService.saveEmployee(employee);

    }


    @GetMapping
    public List<Employee> getAllEmployee(){

        return employeeService.getAllEmployee();

    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id")long empId){

        return employeeService.findEmployeeById(empId)
                .map(ResponseEntity :: ok)
                .orElseGet(()-> ResponseEntity.notFound().build() )  ;

   }


   @PutMapping("{id}")
   public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id , @RequestBody Employee employee ){


        return employeeService.findEmployeeById(id)
                .map(savedEmployee-> {

                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());
                    Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                    return new ResponseEntity<>(updatedEmployee , HttpStatus.OK);

                })
                .orElseGet( ()->
                                ResponseEntity.notFound().build());



   }


   @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id ){

        employeeService.deleteEmployee(id);

        return new ResponseEntity<String>("Employee Deleted Successfully :) " , HttpStatus.OK);

   }


}
