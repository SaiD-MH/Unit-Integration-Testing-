package net.javaguides.springboottesting.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {


    @MockBean
    private EmployeeService employeeService;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private Employee employee ;

    @BeforeEach
    void setup(){
        employee = new Employee( "Mohamed" , "Said","software@google.com");
    }


    @Autowired
    public EmployeeControllerTests(ObjectMapper objectMapper, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    // Junit Test For Create Employee API
    @Test
    @DisplayName("Junit Test For Create Employee API")
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((inv)->inv.getArgument(0));

        //given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);


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


    // Junit Test For GetAllEmployees
    @Test
    @DisplayName("Junit Test For GetAllEmployees")
    public void givenEmployeeList_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception{

        // given - precondition or setup

        List<Employee> employeeList = Arrays.asList(  employee
                , new Employee("myWife", "ms:Mohamed" , "wife@google.com"));


        given(employeeService.getAllEmployee())
                .willReturn(employeeList);



        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()" , is(employeeList.size())));

    }

    // Junit Test For Getting Employee by ID Positive Scenario
    @Test
    @DisplayName("Junit Test For  Getting Employee by ID ")
    public void givenEmployeeId_whenGettingEmployeeById_thenReturnEmployeeObject() throws Exception{

        // given - precondition or setup

        long employeeId = 1L;

        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.of(employee));


        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees/{id}" , employeeId));


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

        given(employeeService.findEmployeeById(employeeId))
                .willReturn(Optional.empty());


        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(get("/api/employees/{id}" , employeeId));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    // Junit Test For Update Employee positive scenario
    @Test
    @DisplayName("Junit Test For Update Employee positive scenario")
    public void givenEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{

        // given - precondition or setup

        Employee savedEmployee = new Employee("mohamed" , "said","mohamed@google.com");
        Employee updatedEmployee = new Employee("Mido" , "Saisss","mido@google.com");
        long empId = 1;

        given(employeeService.findEmployeeById(empId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(savedEmployee)).willReturn(updatedEmployee);


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

        Employee savedEmployee = new Employee("mohamed" , "said","mohamed@google.com");
        Employee updatedEmployee = new Employee("Mido" , "Saisss","mido@google.com");
        long empId = 1;

        given(employeeService.findEmployeeById(empId)).willReturn(Optional.empty());



        // when - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}" , empId)

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        );


        //then - verify the output


        response.andDo(print())
                .andExpect(status().isNotFound());



    }


    // Junit Test For Deleting Employee By Id
    @Test
    @DisplayName("Junit Test For Deleting Employee By Id")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOK200() throws Exception {

        // given - precondition or setup

            long id = 1;
        willDoNothing().given(employeeService).deleteEmployee(id);


        // when - action or the behaviour that we are going to test


        ResultActions response = mockMvc.perform(delete("/api/employees/{id}" , id));


        //then - verify the output

        response.andDo(print())
                .andExpect(status().isOk());

    }


}
