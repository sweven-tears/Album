package com.sweven.annotaion_test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Create by Sweven on 2020/7/10 -- 15:01</p>
 * Email: sweventears@163.com
 */
public class JApp {
    public static void main(String[] args) {
        Test test = new Test();
        System.err.println(test.getStudent().name);
    }

    @Ask("开始测试")
    static
    class Test {
        @Ask("年龄")
        private int age = 12;
        @Ask("学生")
        @Stu(name = "张岚", age = 23)
        private JStudent student;

        @Ask("构造函数")
        public Test() {
            new JInject(this);
        }

        @Ask("创建学生实体")
        public JStudent create(JStudent student) {
            System.out.println("......");
            return this.student = student;
        }

        public JStudent getStudent() {
            return student;
        }
    }

    static class JInject {
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
                    try {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(test, new JStudent());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    String value = ask.value();
                    System.out.println(value);
                }
                Stu stu = declaredMethod.getAnnotation(Stu.class);
                if (stu != null) {
                    try {
                        String name = stu.name();
                        int age = stu.age();
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(test, new JStudent(name, age));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
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
                    System.out.println(declaredField.getName() + "注释:" + value);
                }
                Stu stu = declaredField.getAnnotation(Stu.class);
                if (stu != null) {
                    try {
                        String name = stu.name();
                        int age = stu.age();
                        declaredField.setAccessible(true);
                        declaredField.set(test, new JStudent(name, age));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
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

    @Retention(RetentionPolicy.RUNTIME)
    @interface Stu {
        String name();

        int age();
    }

    static class JStudent {
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

}