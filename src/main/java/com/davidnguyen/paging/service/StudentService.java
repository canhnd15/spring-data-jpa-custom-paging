package com.davidnguyen.paging.service;

import com.davidnguyen.paging.dto.StudentSearchPage;
import com.davidnguyen.paging.dto.StudentSearchSdo;
import com.davidnguyen.paging.utils.paging.Page;

public interface StudentService {
    Page<StudentSearchSdo> getStudents(StudentSearchPage searchPage);
}
