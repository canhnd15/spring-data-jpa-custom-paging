package com.davidnguyen.paging.utils.paging;

import com.davidnguyen.paging.annotation.QueryField;
import com.davidnguyen.paging.exception.AppException;
import com.davidnguyen.paging.utils.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class PagingRepo<T> {
    private final EntityManager em;
    private final Class<T> genericClass;
    private String totalSql;
    private String getSql;

    public PagingRepo(EntityManager em, Class<T> genericClass) {
        this.em = em;
        this.genericClass = genericClass;
    }

    public PagingRepo<T> withTotal(String totalSql) {
        this.totalSql = totalSql;
        return this;
    }

    public PagingRepo<T> withGet(String getSql) {
        this.getSql = getSql;
        return this;
    }

    public Page<T> query(PageInfo pageInfo, Object searchDTO) {
        Query countQuery = em.createNativeQuery(totalSql);
        buildQuery(countQuery, searchDTO);
        Query selectQuery = em.createNativeQuery(getSql, Tuple.class);
        buildQuery(selectQuery, searchDTO);
        return execute(pageInfo, countQuery, selectQuery);
    }

    public Page<T> execute(PageInfo pageInfo, Query countQuery, Query query) {
        int total = ((Long) countQuery.getSingleResult()).intValue();
        query.setFirstResult(pageInfo.getPageSize() * (pageInfo.getCurrentPage() - 1));
        query.setMaxResults(pageInfo.getPageSize());
        List<T> result = DataUtil.convertFromQueryResult(query.getResultList(), genericClass);
        return new Page<T>(pageInfo).withResult(result).compute(total);
    }

    private void buildQuery(Query query, Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(QueryField.class)) {
                    QueryField queryField = field.getDeclaredAnnotation(QueryField.class);
                    if (Objects.nonNull(field.get(object))) {
                        if (queryField.queryLike()) {
                            query.setParameter(queryField.value(), queryLike(field, object));
                        } else {
                            query.setParameter(queryField.value(), isCheckEmpty(field, object, queryField.trim()));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new AppException("API001", ex.getMessage());
        }
    }

    private static Object isCheckEmpty(Field field, Object object, boolean isTrim) {
        try {
            String typeField = field.getType().getTypeName();
            if (typeField.equalsIgnoreCase(String.class.getName())) {
                if (DataUtil.isNullOrEmpty((String) field.get(object))) {
                    return null;
                }
            }
            return isTrim ? ((String) field.get(object)).trim() : field.get(object);
        } catch (Exception ex) {
            throw new AppException("API001", ex.getMessage());
        }
    }

    private Object queryLike(Field field, Object object) {
        try {
            String typeField = field.getType().getTypeName();
            if (typeField.equalsIgnoreCase(String.class.getName())) {
                if (DataUtil.isNullOrEmpty((String) field.get(object))) {
                    return null;
                }
            }
            String value = (String) field.get(object);
            return "%" + value.trim() + "%";
        } catch (Exception ex) {
            throw new AppException("API001", ex.getMessage());
        }
    }
}
