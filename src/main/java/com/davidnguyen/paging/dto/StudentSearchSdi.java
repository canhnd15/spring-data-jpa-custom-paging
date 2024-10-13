package com.davidnguyen.paging.dto;

import com.davidnguyen.paging.annotation.QueryField;
import lombok.Data;

@Data
public class StudentSearchSdi {
    @QueryField(value = "name", queryLike = true)
    public String name;

    @QueryField("code")
    public String code;

    @QueryField(value = "address", queryLike = true)
    public String address;

    @QueryField("phone")
    public String phone;

    @QueryField("age")
    public Integer age;

    @QueryField("score")
    public Integer score;
}
