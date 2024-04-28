package net.javaguides.springboottesting.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {
    private MockMvc mockMvc;

    private EmployeeRepository employeeRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public EmployeeControllerITests(MockMvc mockMvc, EmployeeRepository employeeRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }


    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }


    @Test
    @DisplayName("Integration  Test For Create Employee API")
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup

        Employee employee =
                new Employee("Mohamed" , "Saeed" , "mohamed@gmail.com");



        // when - action or the behaviour that we are going to test


        ResultActions response =  mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName" ,
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName" ,
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email" ,
                        is(employee.getEmail())));

    }


    // Integration Test For GetAllEmployees
    @Test
    @DisplayName("Integration Test For GetAllEmployees")
    public void givenEmployeeList_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception{

        // given - precondition or setup

        Employee employee =
                new Employee("Mohamed" , "Saeed" , "mohamed@gmail.com");

        List<Employee> employeeList = Arrays.asList(  employee
                , new Employee("myWife", "ms:Mohamed" , "wife@google.com"));


        employeeRepository.saveAll(employeeList);

        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()" , is(employeeList.size())));

    }


    // Integration Test For Getting Employee by ID Positive Scenario
    @Test
    @DisplayName("Integration Test For  Getting Employee by ID ")
    public void givenEmployeeId_whenGettingEmployeeById_thenReturnEmployeeObject() throws Exception{

        // given - precondition or setup


        Employee employee =
                new Employee("Mohamed" , "Saeed" , "mohamed@gmail.com");

        employeeRepository.save(employee);
        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees/{id}" , employee.getId()));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName" ,
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName" ,
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email" ,
                        is(employee.getEmail())));
    }




    // Junit Test For Getting Employee by ID Negative Scenario
    @Test
    @DisplayName("Junit Test For  Getting Employee by ID not Found")
    public void givenInvalidEmployeeId_whenGettingEmployeeById_thenReturnEmptyEmployee() throws Exception{

        // given - precondition or setup

        long employeeId = 1L;



        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees/{id}" , employeeId));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isNotFound());

    }



    // Integration Test For Update Employee positive scenario
    @Test
    @DisplayName("Integration Test For Update Employee positive scenario")
    public void givenEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{

        // given - precondition or setup

        Employee savedEmployee = new Employee("mohamed" , "said","mohamed@google.com");
        Employee updatedEmployee = new Employee("Mido" , "Saisss","mido@google.com");


        employeeRepository.save(savedEmployee);

        long empId = savedEmployee.getId();





        // when - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}" , empId)

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        );


        //then - verify the output


        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName" , is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName" , is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email" , is(updatedEmployee.getEmail())));


    }


    // Junit Test For Update Employee -ve scenario
    @Test
    @DisplayName("Junit Test For Update Employee Negative scenario")
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNotFound() throws Exception{

        // given - precondition or setup

        // that in database
        Employee savedEmployee = new Employee("mohamed" , "said","mohamed@google.com");
        // to send in request body As client update my data
        Employee updatedEmployee = new Employee("Mido" , "Saisss","mido@google.com");


        employeeRepository.save(savedEmployee);



        // when - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}" , 1)

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        );


        //then - verify the output


        response.andDo(print())
                .andExpect(status().isNotFound());



    }



    // Integration Test For Deleting Employee By Id
    @Test
    @DisplayName("Integration Test For Deleting Employee By Id")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOK200() throws Exception {

        // given - precondition or setup
        Employee savedEmployee =
                new Employee("mohamed" , "said","mohamed@google.com");

        employeeRepository.save(savedEmployee);
        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(delete("/api/employees/{id}" , savedEmployee.getId()));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isOk());

    }




}
