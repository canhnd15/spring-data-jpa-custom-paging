package com.davidnguyen.paging.dto;

import com.davidnguyen.paging.utils.paging.PageInfo;
import lombok.Data;

@Data
public class StudentSearchPage {
    private StudentSearchSdi sdi;
    private PageInfo pageInfo;
}
