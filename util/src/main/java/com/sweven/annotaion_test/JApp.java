package com.sweven.annotaion_test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * <p>Create by Sweven on 2020/7/10 -- 15:01</p>
 * Email: sweventears@163.com
 */
public class JApp {
    public static void main(String[] args) {
//        Test test = new Test();
//        System.err.println(test.getStudent().name);
        IMenu appMenu = new Menu<JApp>("测试", "a") {
        };
        System.out.println("execute:" + appMenu.getName());
        appMenu.execute(true, 0, "1");
    }

    public static void a(int a, String s) {
        System.out.println("a1!!!!" + s);
    }

    public static void a(Integer a, String s) {
        System.out.println("a!!!!" + s);
    }

}


class Menu<T> implements IMenu {

    private String menuName;
    private String method;
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

    @Override
    public Object execute(boolean pkgType, Object... obj) {
        Class<?> clz = t.getClass();
        try {
            Class<?>[] parameterTypes = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                Class<?> aClass = obj[i].getClass();
                parameterTypes[i] = pkgType(pkgType, aClass);
            }
            Method method = clz.getDeclaredMethod(this.method, parameterTypes);
            return method.invoke(t, obj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private Class<?> pkgType(boolean pkgType, Class<?> aClass) {
        if (!pkgType) return aClass;
        if (aClass == Integer.class) {
            return Integer.TYPE;
        } else return aClass;
    }

    @Override
    public String getName() {
        return menuName;
    }

    @Override
    public String getMethod() {
        return method;
    }

}

interface IMenu {
    Object execute(boolean pkgType, Object... obj);

    String getName();

    String getMethod();
}

@Ask("开始测试")
class Test {
    @Ask("12个月了")
    private int a = 12;
    @Ask("学生")
    private JStudent student;

    @Ask("构造函数")
    public Test() {
        new JInject(this);
    }

    @Ask("创建学生实体")
    public JStudent create(JStudent student) {
        return student;
    }

    public JStudent getStudent() {
        return student;
    }
}

class JInject {
    public JInject(Test test) {
        injectType(test);
        injectField(test);
        injectConstructor(test);
        injectMethod(test);
    }

    private void injectMethod(Test test) {
        Class<? extends Test> aClass = test.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Ask ask = declaredMethod.getAnnotation(Ask.class);
            if (ask != null) {
                String value = ask.value();
                System.out.println(value);
            }
        }
    }

    private void injectConstructor(Test test) {
        for (Constructor<?> declaredConstructor : test.getClass().getDeclaredConstructors()) {
            Ask ask = declaredConstructor.getAnnotation(Ask.class);
            if (ask != null) {
                System.out.println(ask.value());
            }
        }
    }

    private void injectField(Test test) {
        Class<? extends Test> aClass = test.getClass();
        for (Field declaredField : aClass.getDeclaredFields()) {
            Ask ask = declaredField.getAnnotation(Ask.class);
            if (ask != null) {
                String value = ask.value();
                if (declaredField.getType() == JStudent.class) {
                    JStudent haha = test.create(new JStudent("haha", 22));
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(test, haha);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(declaredField.getName() + ":" + value);
            }
        }
    }

    private void injectType(Test test) {
        Ask ask = test.getClass().getAnnotation(Ask.class);
        if (ask != null) {
            System.out.println(ask.value());
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Ask {
    String value();
}

class JStudent {
    public String name;
    public int age;

    public JStudent(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public JStudent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
