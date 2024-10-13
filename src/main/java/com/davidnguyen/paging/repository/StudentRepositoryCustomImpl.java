package com.davidnguyen.paging.repository;

import com.davidnguyen.paging.dto.StudentSearchPage;
import com.davidnguyen.paging.dto.StudentSearchSdo;
import com.davidnguyen.paging.utils.DataUtil;
import com.davidnguyen.paging.utils.paging.Page;
import com.davidnguyen.paging.utils.paging.PagingRepo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<StudentSearchSdo> findByCondition(StudentSearchPage searchPage) {
        StringBuilder sqlCount = new StringBuilder()
                .append("select count(1) ");

        StringBuilder sqlQuery = new StringBuilder()
                .append("select  ")
                .append(" s.id, ")
                .append(" s.name, ")
                .append(" s.code, ")
                .append(" s.address, ")
                .append(" s.phone, ")
                .append(" s.age, ")
                .append(" s.score  ");

        StringBuilder sqlCondition = new StringBuilder()
                .append("from student s ")
                .append("where 1=1 ")
                .append(Objects.nonNull(searchPage.getSdi().name) ? " and (s.name like :name) " : "")
                .append(Objects.nonNull(searchPage.getSdi().code) ? " and (s.code = :code) " : "")
                .append(Objects.nonNull(searchPage.getSdi().address) ? " and (s.address like :address) " : "")
                .append(Objects.nonNull(searchPage.getSdi().phone) ? " and (s.phone = :phone) " : "")
                .append(Objects.nonNull(searchPage.getSdi().age) ? " and (s.age = :age) " : "")
                .append(Objects.nonNull(searchPage.getSdi().score) ? " and (s.score <= :score) " : "");

        PagingRepo<StudentSearchSdo> pagingRepo = new PagingRepo<>(entityManager, StudentSearchSdo.class)
                .withGet(sqlQuery.append(sqlCondition).toString())
                .withTotal(sqlCount.append(sqlCondition).toString());

        return pagingRepo.query(searchPage.getPageInfo(), searchPage.getSdi());
    }
}
