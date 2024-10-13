package com.davidnguyen.paging.service;

import com.davidnguyen.paging.dto.StudentSearchPage;
import com.davidnguyen.paging.dto.StudentSearchSdo;
import com.davidnguyen.paging.repository.StudentRepository;
import com.davidnguyen.paging.utils.paging.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Page<StudentSearchSdo> getStudents(StudentSearchPage searchPage) {
        return studentRepository.findByCondition(searchPage);
    }
}
