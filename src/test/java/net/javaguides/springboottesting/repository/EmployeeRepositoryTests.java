package net.javaguides.springboottesting.repository;

import net.javaguides.springboottesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {



    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup(){

         employee = new Employee("Mohamed" , "Saeed" , "software@google.com");
    }


    @Autowired
    public EmployeeRepositoryTests(EmployeeRepository employeeRepository){

        this.employeeRepository = employeeRepository;

    }


    // Junit Test for save Employee
    @Test
    @DisplayName("Unit Test for EmployeeRepo.save() Method")
    public void givenEmployee_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        // when - action or the behaviour that we are going to test

        Employee savedEmployee =employeeRepository.save(employee);

        //then - verify the output


        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);



    }

    // Junit Test for findAll Employees
    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList(){

        // given - precondition or setup


        Employee employee1 = new Employee("Mohamed" , "Saeed" , "software@google.com");

        Employee employee2 = new Employee("Khaled" , "Ziad" , "Backend@google.com");

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);




        // when - action or the behaviour that we are going to test

        List<Employee> employeeList = employeeRepository.findAll();


        //then - verify the output

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit Test For FindById operation
    @Test
    @DisplayName("Junit Test For FindById operation")
    public void givenEmployee_whenFindById_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test

         Employee dbEmployee = employeeRepository.findById(employee.getId()).get();

        //then - verify the output

        assertThat(dbEmployee).isNotNull();
    }


    // Junit Test For FindByEmail
    @Test
    public void givenEmployee_whenFindByEmail_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);


        // when - action or the behaviour that we are going to test

        Employee employeDb = employeeRepository.findByEmail(employee.getEmail()).get();


        //then - verify the output

        assertThat(employeDb).isNotNull();
    }


    // Junit Test For Update employee
    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        // given - precondition or setup
        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

         employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test

        Employee dbEmployee = employeeRepository.findById(employee.getId()).get();

        dbEmployee.setFirstName("Sarah");
        dbEmployee.setEmail("sarah@amazon.com");

        Employee updatedEmployee = employeeRepository.save(dbEmployee);

        //then - verify the output


        assertThat(updatedEmployee.getEmail()).isEqualTo("sarah@amazon.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Sarah");

    }


    // Junit Test For delete employee
    @Test
    @DisplayName("Junit Test For delete employee")
    public void givenEmployee_whenDelete_thenDeletedSuccessfully(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);


        // when - action or the behaviour that we are going to test

        employeeRepository.delete(employee);
        Optional<Employee>employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output

        assertThat(employeeOptional).isEmpty();


    }

    // Junit Test For Custom JPQL  Query By index
    @Test
    @DisplayName("Junit Test For Custom JPQL  Query By index")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);

        String firstName = "Mohamed" , lastName = "Saeed";



        // when - action or the behaviour that we are going to test

        Employee dbEmployee = employeeRepository.findByJPQL(firstName , lastName);


        //then - verify the output

        assertThat(dbEmployee).isNotNull();

    }


    // Junit Test For Custom JPQL  Query Named Params
    @Test
    @DisplayName("Junit Test For Custom JPQL  Query Named Params")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);

        String firstName = "Mohamed" , lastName = "Saeed";



        // when - action or the behaviour that we are going to test

        Employee dbEmployee = employeeRepository.findByJPQLNamedParams(firstName , lastName);


        //then - verify the output

        assertThat(dbEmployee).isNotNull();

    }


    // Junit Test For findBy firstName and LastName with index native Sql
    @Test
    @DisplayName(" Junit Test For findBy firstName and LastName with index native Sql")
    public void givenEmployeeFirstnameAndLastName_whenFindByNativeSqlWithIndex_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);

        String firstName = "Mohamed" , lastName = "Saeed";

        // when - action or the behaviour that we are going to test


        Employee dbEmployee = employeeRepository.findByNativeSqlWithIndex(firstName , lastName);


        //then - verify the output
        assertThat(dbEmployee).isNotNull();


    }


    // Junit Test For findBy firstName and LastName with Named Params native Sql
    @Test
    @DisplayName(" Junit Test For findBy firstName and LastName with Named Params native Sql")
    public void givenEmployeeFirstnameAndLastName_whenFindByNativeSqlWithNamedParams_thenReturnEmployee(){

        // given - precondition or setup

        // Employee employee = new Employee("Mohamed" , "Saeed" , "software@google.com");

        employeeRepository.save(employee);

        String firstName = "Mohamed" , lastName = "Saeed";

        // when - action or the behaviour that we are going to test


        Employee dbEmployee = employeeRepository.findByNativeSqlWithNamedParam(firstName , lastName);


        //then - verify the output
        assertThat(dbEmployee).isNotNull();


    }


}
