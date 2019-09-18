package luoluna.sweven.album.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class Verifier {

    /**
     * 判断某个值是否存在该对象集合中
     *
     * @param list       集合
     * @param o          查询的数据
     * @param methodName 对象中的get方法名
     * @param <E>        泛型对象
     * @return 是否存在
     */
    public static <E> boolean contains(List<E> list, Object o, String methodName) {
        for (E e : list) {
            try {
                Class cls = e.getClass();
                Method[] methods = cls.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        Object value = method.invoke(e);
                        if (Objects.equals(value, o)) {
                            return true;
                        }
                    }
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
