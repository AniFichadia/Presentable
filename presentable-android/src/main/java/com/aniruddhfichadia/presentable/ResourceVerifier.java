package com.aniruddhfichadia.presentable;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 3/1/17
 */
public class ResourceVerifier {
    public static void verifyResourcesPresent(Class<?> androidResFileClass, Class<?> presentableResClass)
            throws RuntimeException {
        Field[] declaredFields = presentableResClass.getDeclaredFields();


        List<Field> missingFields = new ArrayList<>();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            try {
                androidResFileClass.getField(fieldName);
            } catch (NoSuchFieldException e) {
                missingFields.add(field);
            }
        }

        if (!missingFields.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following fields are not declared in ")
                    .append(androidResFileClass.getName());

            for (Field missingField : missingFields) {
                sb.append('\n');
                sb.append("\t - ");
                sb.append(missingField.getName());
            }

            throw new RuntimeException(sb.toString());
        }
    }
}