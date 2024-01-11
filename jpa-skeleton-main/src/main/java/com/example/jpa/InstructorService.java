package com.example.jpa;

import com.example.jpa.dto.InstructorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InstructorService {

    public InstructorDto readInstructor(Long id) {
        return new InstructorDto();
    }

    public List<InstructorDto> readInstructorAll() {
        return new ArrayList<>();
    }

}
