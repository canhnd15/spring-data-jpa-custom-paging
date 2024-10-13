package com.davidnguyen.paging.controller;

import com.davidnguyen.paging.dto.StudentSearchPage;
import com.davidnguyen.paging.dto.StudentSearchSdo;
import com.davidnguyen.paging.service.StudentService;
import com.davidnguyen.paging.utils.paging.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<Page<StudentSearchSdo>> getStudents(@RequestBody StudentSearchPage searchPage) {
        return ResponseEntity.ok().body(studentService.getStudents(searchPage));
    }
}
