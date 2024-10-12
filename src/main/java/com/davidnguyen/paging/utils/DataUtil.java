package com.laptrinhb2a.PagingCustom.utils;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;

public class DataUtil {
    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            List<T> result = new ArrayList<>();

            for (Tuple sourceItem : listSource) {
                Constructor<?> cons = classTarget.getConstructor();
                Object target = cons.newInstance();

                Field[] targetFields = target.getClass().getDeclaredFields();


                for (Field targetFieldItem : targetFields) {
                    setFieldData(sourceItem, targetFieldItem, target, dateFormats);
                }

                result.add((T) target);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return convertFromQueryResult(listSource, classTarget, dateFormats);
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget) {
        return convertFromQueryResult(listSource, classTarget, Const.DATE_FORMAT);
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();

            Field[] targetFields = target.getClass().getDeclaredFields();


            for (Field targetFieldItem : targetFields) {
                setFieldData(source, targetFieldItem, target, dateFormats);
            }

            return ((T) target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return convertFromQueryResult(source, classTarget, dateFormats);
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget) {
        return convertFromQueryResult(source, classTarget, Const.DATE_FORMAT);
    }

    private static void setFieldData(
            Tuple sourceItem, Field targetFieldItem, Object target, Map<String, String> dateFormats
    ) throws IllegalAccessException, ParseException {
        List<TupleElement<?>> sourceFields = sourceItem.getElements();

        String fieldName = targetFieldItem.getName().toLowerCase();

        targetFieldItem.setAccessible(true);
        Class<?> targetFieldType = targetFieldItem.getType();
        String targetFieldTypeName = targetFieldType.getName();

        for (TupleElement<?> sourceFieldItem : sourceFields) {
            String sourceFieldName = sourceFieldItem.getAlias();
            String sourceFieldNameRemoveUnderscore = sourceFieldItem.getAlias().replace("_", "");

            if (!fieldName.equalsIgnoreCase(sourceFieldNameRemoveUnderscore)) continue;

            String dateFormat;
            if (dateFormats.size() == 1) {
                dateFormat = dateFormats.get("ALL");
            } else {
                dateFormat = dateFormats.get(sourceFieldName);
            }

            Object sourceItemData = sourceItem.get(sourceFieldName);
            if (sourceItemData == null) continue;
            if (String.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                if (sourceItemData instanceof String || sourceItemData instanceof Long || sourceItemData instanceof Character) {
                    targetFieldItem.set(target, DataUtil.safeToString(sourceItemData));
                } else if (sourceItemData instanceof Date || sourceItemData instanceof java.sql.Timestamp) {
                    targetFieldItem.set(target, DateUtil.date2StringByPattern(DataUtil.safeToDate(sourceItemData), dateFormat));
                }
            } else if (Long.class.getName().equalsIgnoreCase(targetFieldTypeName) || long.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, DataUtil.safeToLong(sourceItemData));
            } else if (Double.class.getName().equalsIgnoreCase(targetFieldTypeName) || double.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, DataUtil.safeToDouble(sourceItemData));
            } else if (Boolean.class.getName().equalsIgnoreCase(targetFieldTypeName) || boolean.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, "true".equalsIgnoreCase(DataUtil.safeToString(sourceItemData)));
            } else if (Date.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, DataUtil.safeToDate(sourceItemData));
            } else if (BigDecimal.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, DataUtil.safeToBigDecimal(sourceItemData));
            } else if (Integer.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, DataUtil.safeToInt(sourceItemData));
            }
        }
    }

    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }

        return obj1.toString().trim();
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Date safeToDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (Date) obj1;
    }

    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        if (obj1 instanceof BigDecimal) {
            return ((BigDecimal) obj1).longValue();
        }
        if (obj1 instanceof BigInteger) {
            return ((BigInteger) obj1).longValue();
        }
        if (obj1 instanceof Double) {
            return ((Double) obj1).longValue();
        }

        try {
            return Long.parseLong(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static Double safeToDouble(Object obj1, Double defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static BigDecimal safeToBigDecimal(Object obj1) {
        if (obj1 == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(obj1.toString());
        } catch (final NumberFormatException nfe) {
            return BigDecimal.ZERO;
        }
    }

    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    public static int safeToInt(Object obj1, int defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            String s = obj1.toString();
            if (s.contains(".")) s = s.substring(0, s.lastIndexOf("."));

            return Integer.parseInt(s);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static String convertToLikeConditional(String conditional) {
        conditional = conditional.replace("%", "\\%").replace("_", "\\_");

        return "%" + conditional + "%";
    }
}
