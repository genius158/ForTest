### A和B两个组件之间不相互依赖，在A组件想访问和使用B组件的的类，使用运行时注解实现
    
```
@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface TargetClass {
    String value();
}

public class Proxy {
    /**
     * 代理的目标对象
     */
    public Object original;

    /**
     * 代理的目标对象 类型
     */
    private Class<?> originalClass;

    /**
     * 如果目标类没有引入，在创建和执行都将抛异常，用到的地方要多次catch
     * 为了方便 统一调用的时候抛异常
     */
    private Throwable lazyReportError;

     /**
     * 使用运行时注解 有些多余
     * 直接传入 目标类名 Class.forName 就完了~~
     *
     * @param clazz fakeClass
     */
    public Proxy(Class<?> clazz) {
        // 找到对应的注解
        TargetClass targetClass = clazz.getAnnotation(TargetClass.class);
        if (targetClass == null) {
            lazyReportError = new RuntimeException("TargetClass is set ? in " + clazz.getCanonicalName());
            return;
        }
        try {
            originalClass = Class.forName(targetClass.value());
            original = originalClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            lazyReportError = e;
        }
    }

    public void execFunc(String method, Class<?>[] parameterTypes, Object... args) throws Throwable {
        if (lazyReportError != null){
            throw new IllegalStateException("error during initialization", lazyReportError);
        }
        try {
            Method m = originalClass.getDeclaredMethod(method, parameterTypes);
            m.setAccessible(true);
            m.invoke(original, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw e;
        }
    }
}

// 组件 B 里的目标类
// 类名、方法、成员变量 都不能混淆
public class Original {
    public void test() {
    }
}

// 组件 A 里的Fake类
@TargetClass("*.Original")
public class FakeClass {
    public final Proxy proxy;

    public FakeClass() {
        proxy = new Proxy(getClass());
    }

    public void test() throws Throwable {
        proxy.execFunc("test", null, null);
    }
}

// 组件A 测试代码
// 壳工程同时引入A、B两个模块，能够调用到目标类对应的方法
// 不引入B组件，走到异常路径
void logic() {
    FakeClass fakeClass = new FakeClass();
    try {
        fakeClass.test();
    } catch (Throwable e) {
        e.printStackTrace();
    }
}
```
