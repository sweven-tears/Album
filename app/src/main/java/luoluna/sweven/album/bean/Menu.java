package luoluna.sweven.album.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Menu {
    private String name;
    private String method;

    public <Type, E> Type execute(E e, Type type) {
        Class t = type.getClass();
        try {
            Class clz = e.getClass();
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(this.method)) {
                    return (Type) method.invoke(e);
                }
            }
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return type;
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
