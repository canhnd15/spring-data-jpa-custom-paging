package com.davidnguyen.paging.repository;

import com.davidnguyen.paging.dto.StudentSearchPage;
import com.davidnguyen.paging.dto.StudentSearchSdo;
import com.davidnguyen.paging.utils.paging.Page;

public interface StudentRepositoryCustom {
    Page<StudentSearchSdo> findByCondition(StudentSearchPage searchPage);
}
