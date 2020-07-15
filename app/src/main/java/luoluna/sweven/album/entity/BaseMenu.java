package luoluna.sweven.album.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class BaseMenu<T> {
    private String name;
    private String method;
    private T t;

    public BaseMenu() {
        init();
    }

    public BaseMenu(String name, String method) {
        this.name = name;
        this.method = method;
        init();
    }

    private void init() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            if (superclass.getActualTypeArguments().length < 2) return;
            t = ((Class<T>) superclass.getActualTypeArguments()[0]).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public Object execute(Object... obj) {
        try {
            Class<?> clz = t.getClass();
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(this.method)) {
                    return method.invoke(name, obj);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
