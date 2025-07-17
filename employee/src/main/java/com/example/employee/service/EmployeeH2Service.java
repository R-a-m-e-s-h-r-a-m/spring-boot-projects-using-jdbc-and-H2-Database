/*
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 * 
 */

// Write your code here


package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRowMapper;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeH2Service implements EmployeeRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Employee> getEmployees() {
        List<Employee> employeeList = db.query("select * from EMPLOYEELIST order by employeeId", new EmployeeRowMapper());
        return new ArrayList<>(employeeList);
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try {
            return db.queryForObject("select * from EMPLOYEELIST where employeeId = ?", new EmployeeRowMapper(), employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Employee addEmployee(Employee employee) {
        db.update("insert into EMPLOYEELIST(employeeName, email, department) values(?,?,?)",
            employee.getEmployeeName(),
            employee.getEmail(),
            employee.getDepartment());
        // Retrieve the newly added employee by matching on its details
        Employee savedEmployee = db.queryForObject(
            "select * from EMPLOYEELIST where employeeName = ? and email = ? and department = ?",
            new EmployeeRowMapper(),
            employee.getEmployeeName(),
            employee.getEmail(),
            employee.getDepartment());
        return savedEmployee;
    }

    @Override
    public Employee updateEmployee(int employeeId, Employee employee) {
        int updated = db.update("update EMPLOYEELIST set employeeName = ?, email = ?, department = ? where employeeId = ?",
            employee.getEmployeeName(),
            employee.getEmail(),
            employee.getDepartment(),
            employeeId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getEmployeeById(employeeId);
    }

    @Override
    public void deleteEmployee(int employeeId) {
        // Simply perform the delete. Even if no record is deleted, return OK.
        db.update("delete from EMPLOYEELIST where employeeId = ?", employeeId);
    }
}
