package com.davidnguyen.paging.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchSdo {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String phone;
    private Integer age;
    private Integer score;
}
