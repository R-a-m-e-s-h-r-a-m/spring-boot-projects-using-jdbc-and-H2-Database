package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentH2Service implements StudentRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Student> getStudents() {
        String sql = "SELECT * FROM STUDENT ORDER BY studentId";
        List<Student> students = db.query(sql, new StudentRowMapper());
        return new ArrayList<>(students);
    }

    @Override
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM STUDENT WHERE studentId = ?";
        try {
            return db.queryForObject(sql, new StudentRowMapper(), studentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    @Override
    public Student addStudent(Student student) {
        String sql = "INSERT INTO STUDENT(studentName, gender, standard) VALUES(?, ?, ?)";
        db.update(sql, student.getStudentName(), student.getGender(), student.getStandard());
        int id = db.queryForObject("CALL IDENTITY()", Integer.class);
        return getStudentById(id);
    }

    @Override
    public int addStudentsBulk(ArrayList<Student> students) {
        String sql = "INSERT INTO STUDENT(studentName, gender, standard) VALUES(?, ?, ?)";
        int[] updateCounts = db.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Student s = students.get(i);
                ps.setString(1, s.getStudentName());
                ps.setString(2, s.getGender());
                ps.setInt(3, s.getStandard());
            }

            @Override
            public int getBatchSize() {
                return students.size();
            }
        });
        return updateCounts.length;
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        String sql = "UPDATE STUDENT SET studentName = ?, gender = ?, standard = ? WHERE studentId = ?";
        int updated = db.update(sql, student.getStudentName(), student.getGender(), student.getStandard(), studentId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        return getStudentById(studentId);
    }

    @Override
    public void deleteStudent(int studentId) {
        db.update("DELETE FROM STUDENT WHERE studentId = ?", studentId);
    }
}
