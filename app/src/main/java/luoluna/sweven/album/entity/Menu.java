package luoluna.sweven.album.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu<T> {
    protected String menuName;
    protected String method;
    private T t;

    public Menu(String name, String method) {
        this.menuName = name;
        this.method = method;
        init();
    }

    private void init() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            if (superclass.getActualTypeArguments().length < 1) return;
            t = ((Class<T>) superclass.getActualTypeArguments()[0]).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public Object execute(Object... obj) {
        try {
            Class<?> clz = t.getClass();
            List<Class<?>> classes = null;
            if (obj != null) {
                classes = new ArrayList<>();
                for (Object o : obj) {
                    classes.add(o.getClass());
                }
            }
            Method method = null;
            if (classes != null) {
                clz.getDeclaredMethod(this.method, classes.toArray(new Class[0]));
            } else {
                method = clz.getDeclaredMethod(this.method);
            }
            if (method != null && method.getName().equals(this.method)) {
                return method.invoke(t, obj);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
