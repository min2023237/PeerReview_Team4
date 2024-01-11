package com.example.jpa.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private Integer age;
    private String phone;
    private String email;
}
