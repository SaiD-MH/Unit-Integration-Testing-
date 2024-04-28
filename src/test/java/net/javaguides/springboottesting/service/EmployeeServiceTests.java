package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.exception.ResourceNotFoundException;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.Impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {


    @Mock
    private EmployeeRepository employeeRepository ;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setup(){

        /* -------------- With @Annotations we don't need that anymore ----------------
                employeeRepository = Mockito.mock(EmployeeRepository.class);
                employeeService = new EmployeeServiceImpl(employeeRepository);
         */



        employee = new Employee(1L , "Mohamed" ,"Said" , "software@google.com");



    }


    // Junit Test For saving EmployeeService employee
    @Test
    @DisplayName("Junit Test For saving EmployeeService employee")
    public void givenEmployee_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);


        // when - action or the behaviour that we are going to test

        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output

        assertThat(savedEmployee).isNotNull();


    }




    // Junit Test For saving EmployeeService employee which throw exception
    @Test
    @DisplayName("Junit Test For saving EmployeeService employee Throw resource Exception")
    public void givenExistingEmail_whenSave_thenReturnThrowException(){

        // given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test


        Assertions.assertThrows( ResourceNotFoundException.class,
                ()-> employeeService.saveEmployee(employee));

        //then - verify the output

        verify(employeeRepository,never()).save(any(Employee.class));

    }


    // Junit Test For get All employees
    @Test
    @DisplayName("Junit Test For get All employees")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){

        // given - precondition or setup

        Employee employee1 =
                new Employee(2L , "Mai" ,"Ibrahim" , "wife@google.com");

        given(employeeRepository.findAll()).
                // willReturn(Arrays.asList(employee1 , employee));
                willReturn(List.of(employee1 , employee)
        );

        // when - action or the behaviour that we are going to test


        List<Employee> employeeList = employeeService.getAllEmployee();


        //then - verify the output

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }


    // Junit Test For get All employees when empty list ( -ve Scenario )
    @Test
    @DisplayName("Junit Test For get All employees when empty list ( -ve Scenario )")
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){

        // given - precondition or setup

        given(employeeRepository.findAll()).
               willReturn(Collections.EMPTY_LIST);

        // when - action or the behaviour that we are going to test


        List<Employee> employeeList = employeeService.getAllEmployee();


        //then - verify the output

        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);

    }


    // Junit Test For Get Employee By Id
    @Test
    @DisplayName("Junit Test For Get Employee By Id")
    public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployee(){

        // given - precondition or setup

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));


        // when - action or the behaviour that we are going to test

        Employee dbEmployee = employeeService.findEmployeeById(employee.getId()).get();


        //then - verify the output

        assertThat(dbEmployee).isNotNull();


    }

    // Junit Test For Update employee
    @Test
    @DisplayName("Junit Test For UpdateEmployee")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        // given - precondition or setup

        given(employeeRepository.save(employee)).willReturn(employee);

        employee.setFirstName("Marwa");
        employee.setLastName("Ahmed");

        // when - action or the behaviour that we are going to test


        Employee updatedEmployee = employeeService.updateEmployee(employee);


        //then - verify the output

        assertThat(updatedEmployee.getFirstName()).isEqualTo("Marwa");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Ahmed");


    }



    // Junit Test For delete employee by id
    @Test
    @DisplayName("Junit Test For delete employee by id ")
    public void givenEmployeeId_whenDeleteById_thenDoNothing(){

        // given - precondition or setup

        long empId = 1;
        willDoNothing().given(employeeRepository).deleteById(empId);


        // when - action or the behaviour that we are going to test


        employeeService.deleteEmployee(empId);

        //then - verify the output

        verify(employeeRepository , times(1)).deleteById(empId);
    }



}
