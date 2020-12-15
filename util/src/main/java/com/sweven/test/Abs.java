package com.sweven.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Sweven
 * @date 2020/12/10 -- 15:58
 * Email: sweventears@163.com
 */
public abstract class Abs<T> {
    private Type clazz;

    public Abs() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = superclass.getActualTypeArguments();
        Type argument = arguments[0];
        try {
            clazz = argument;
            T print = print(clazz);
            Type actualTypeArgument = ((ParameterizedType) print.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            System.out.println(actualTypeArgument);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected abstract T print(Type name);
}
