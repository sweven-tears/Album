package com.sweven.annotation_test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.sweven.annotation_test.Param.classBy;

/**
 * <p>Create by Sweven on 2020/7/10 -- 15:01</p>
 * Email: sweventears@163.com
 */
public class JApp {
    public static void main(String[] args) {
        Menu<JApp> jAppMenu = new Menu<JApp>("test method", "app") {
        };
        Object execute = jAppMenu.
                execute(
                        Param.build(3L, false),
                        Param.build(3.5f, true));
        System.out.println(execute);
        jAppMenu = new Menu<JApp>("test2", "student") {
        };
        jAppMenu.addParam(34).asBasicDataType()
                .addParam(new JStudent("127", 5), JStudent.class)
                .execute();
        System.out.println(JApp.class.getName());

    }

    public void student(int addAge, JStudent student) {
        student.setAge(student.age + addAge);
        System.out.println(student.name + "有" + student.age + "岁.");
    }

    public String app(long c, Float d) {
        return d + "app:" + c;
    }

    public void count(int a, double c) {
        System.out.println(a + c);
    }

}

abstract class Menu<T> {
    private String menuName;
    private String method;
    private T t;
    private List<Param> params = new ArrayList<>();

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

    public Menu<T> addParam(Object object, boolean pkgType) {
        params.add(Param.build(object, pkgType));
        return this;
    }

    public Menu<T> addParam(Object object, String className) {
        params.add(Param.build(object, className));
        return this;
    }

    public Menu<T> addParam(Object object) {
        params.add(Param.build(object));
        return this;
    }

    public Menu<T> addParam(Object object, Class<?> classType) {
        params.add(Param.build(object, classType));
        return this;
    }

    public Menu<T> asBasicDataType() {
        params.get(params.size() - 1).asBasicDataType();
        return this;
    }

    public Object execute() {
        try {
            Class<?> clz = t.getClass();
            Class<?>[] parameterTypes = new Class[params.size()];
            Object[] objects = new Object[params.size()];
            for (int i = 0; i < params.size(); i++) {
                parameterTypes[i] = params.get(i).getType();
                objects[i] = params.get(i).getObject();
            }
            Method method = clz.getDeclaredMethod(this.method, parameterTypes);
            return method.invoke(t, objects);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public Object execute(Param... params) {
        try {
            Class<?> clz = t.getClass();
            Class<?>[] parameterTypes = new Class[params.length];
            Object[] objects = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                parameterTypes[i] = params[i].getType();
                objects[i] = params[i].getObject();
            }
            Method method = clz.getDeclaredMethod(this.method, parameterTypes);
            return method.invoke(t, objects);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public Object execute(Object... obj) {
        try {
            Class<?> clz = t.getClass();
            Class<?>[] parameterTypes = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                parameterTypes[i] = obj[i].getClass();
            }
            Method method = clz.getDeclaredMethod(this.method, parameterTypes);
            method.setAccessible(true);
            return method.invoke(t, obj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public Object execute(boolean pkgType, Object... obj) {
        try {
            Class<?> clz = t.getClass();
            Class<?>[] parameterTypes = new Class[obj.length];
            for (int i = 0; i < obj.length; i++) {
                parameterTypes[i] = classBy(obj[i].getClass(), pkgType);
            }
            Method method = clz.getDeclaredMethod(this.method, parameterTypes);
            method.setAccessible(true);
            return method.invoke(t, obj);
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

class Param {
    private Object object;
    private Class<?> type;

    private Param(Object object, Class<?> type) {
        this.object = object;
        this.type = type;
    }

    public static Param build(Object object, Class<?> type) {
        return new Param(object, type);
    }

    /**
     * @param object  参数
     * @param pkgType 基本类型是否为包装类
     * @return
     */
    public static Param build(Object object, boolean pkgType) {
        Class<?> aClass = object.getClass();//boolean、byte、short、int、long、char、float、double
        return new Param(object, classBy(aClass, pkgType));
    }

    public static Class<?> classBy(Class<?> aClass, boolean pkgType) {
        if (pkgType) return aClass;
        if (aClass == Integer.class) aClass = int.class;
        else if (aClass == Boolean.class) aClass = boolean.class;
        else if (aClass == Byte.class) aClass = byte.class;
        else if (aClass == Short.class) aClass = short.class;
        else if (aClass == Long.class) aClass = long.class;
        else if (aClass == Character.class) aClass = char.class;
        else if (aClass == Float.class) aClass = float.class;
        else if (aClass == Double.class) aClass = double.class;
        return aClass;
    }

    public static Param build(Object object, String basicDataTypeName) {
        Class<?> aClass = null;
        switch (basicDataTypeName) {
            case "int":
                aClass = int.class;
                break;
            case "boolean":
                aClass = boolean.class;
                break;
            case "byte":
                aClass = byte.class;
                break;
            case "sort":
                aClass = short.class;
                break;
            case "long":
                aClass = long.class;
                break;
            case "char":
                aClass = char.class;
                break;
            case "float":
                aClass = float.class;
                break;
            case "double":
                aClass = double.class;
                break;
            default:
                try {
                    aClass = Class.forName(basicDataTypeName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }
        return new Param(object, aClass);
    }

    public static Param build(Object object) {
        return new Param(object, object.getClass());
    }

    public void asBasicDataType() {
        setType(classBy(type, true));
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getType() {
        return type;
    }
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
