package com.example.jpa;

import com.example.jpa.dto.StudentDto;
import com.example.jpa.entity.Student;
import com.example.jpa.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public void create(
            String name,
            Integer age,
            String phone,
            String email
    ) {
        // 주어진 정보로 새로운 Student 객체를 만든다.
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setPhone(phone);
        student.setEmail(email);
        // repository의 save 메서드를 호출한다.
        studentRepository.save(student);
    }

    public Student readStudent(Long id) {
        Optional<Student> optionalStudent
                = studentRepository.findById(id);
        // 실제 데이터가 있으면 해당 데이터를,
        return optionalStudent
                // 없으면 null을 반환한다.
                .orElse(null);
    }

    public List<Student> readStudentAll() {
        List<Student> students = studentRepository.findAll();
//        for (Student student: students) {
//            System.out.println(student.toString());
//        }
        return students;
    }

    public void update(
            // 수정할 데이터의 PK가 무엇인지
            Long id,
            // 수정할 데이터
            String name,
            Integer age,
            String phone,
            String email
    ) {
        // 1. 업데이트할 대상 데이터를 찾고,
        Student target = readStudent(id);
        // 2. 데이터의 내용을 전달받은 내용으로 갱신하고,
        target.setName(name);
        target.setAge(age);
        target.setPhone(phone);
        target.setEmail(email);
        // 3. repository를 이용해 저장한다.
        studentRepository.save(target);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
