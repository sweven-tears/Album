# 手写注解框架

## 前言
以前看到直接来个@BindView(R.id.view)就把组件的id找着了，觉得很神奇。后来才知道，原来这就是注解，方便快捷，了解了后知道这是开源的ButterKnife框架，很好用。加上上个月看了相关的视频教程，原来并没有我想象的困难，于是今天就来手写注解框架开始实践吧！

## 认识
了解以下annotation怎么写：
1.首先先来了解下什么是元注解：
元注解就是注解的注解，先看看代码：
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//元注解
@Retention(RetentionPolicy.RUNTIME)//元注解
public @interface BindView {
    int value();
}
```
2.`@Target`的作用是描述注解的使用范围，如何没有加这个元注解，就表示该注解可以在任何地方使用。`ElementType`有以下参数：
```java
    TYPE,// 类, 接口 (注解), 或者枚举
    FIELD,// 字段、枚举的常量
    METHOD,// 方法
    PARAMETER,// 方法参数
    CONSTRUCTOR,// 构造方法
    LOCAL_VARIABLE,// 局部变量
    ANNOTATION_TYPE,// 注解
    PACKAGE,// 包
    TYPE_PARAMETER,// 该注解能写在类型变量的声明语句中，java8之后
    TYPE_USE// 该注解能使用在使用类型的任何语句中，java8之后
```
3.`@Retention`指示带注释类型的注释要保留多长时间。如果注释类型声明中没有保留注释，保留策略默认为`RetentionPolicy.CLASS`。只有当元注释类型直接用于注释时，保留元注释才有效。如果元注释类型被用作另一个注释类型中的成员类型，则没有任何效果。`RetentionPolicy`的参数如下：
```java
SOURCE,//编译时进行阅读理解，查看
CLASS,//注释将由编译器记录在类文件中，但不需要在运行时由虚拟机保留。
RUNTIME//注释将由编译器记录在类文件中，并在运行时由虚拟机保留，因此它们可以被反射性地读取。一直存在
```
## 创建一个Inject类来处理注解
```java
/**
 * 手写注解
 * <p>Create by Sweven on 2020/7/8 -- 14:27</p>
 * Email: sweventears@163.com
 */
public class Inject {
    public static void init(Activity activity) {
        injectContent(activity);
        injectView(activity);
    }

    private static void injectContent(Activity activity) {
        Class<? extends Context> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            activity.setContentView(layoutId);
        }
    }

    private static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();// 获得类中的常量
        for (Field field : declaredFields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                int resId = bindView.value();
                View view = activity.findViewById(resId);
                field.setAccessible(true);
                try {
                    field.set(activity, view);// 将view反射到field中去
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

}
```
## 使用
好了，现在可以找一个Activity中测试一下：
```java
@ContentView(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {

    @BindView(R.id.image)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.init(this);//简单的手写注解，这一句必须添加
        imageView.setImageResource(R.drawable.ic_launch);
    }
	//...
}
```
## 结语
这只是注解的一种简单用法。高级的还需要`Annotation Processor Tool`、`AbstractProcessor`来操作。
现就这样。