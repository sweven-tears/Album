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
                Method[] methods = cls.getDeclaredMethods();
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

    /**
     * 判断list中某一对象是否与
     * {@linkplain R r}对象中所有值相同
     */
    public static <R> boolean contains(List<R> list, R r) {
        for (R i : list) {
            try {
                Class cls = r.getClass();
                Class cls2=i.getClass();
                Method[] methods = cls.getDeclaredMethods();
                Method[] methods2 = cls2.getDeclaredMethods();
                int result = 0;
                for (Method method : methods) {
                    if (Objects.equals(method.invoke(i), method.invoke(r))) {
                        result++;
                    }
                }
                for (int i1 = 0; i1 < methods.length; i1++) {

                }
                if (result == methods.length) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void delAlbum(){

    }

}
