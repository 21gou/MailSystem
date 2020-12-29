package reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {
    Object target = null;

    private DynamicProxy(Object target) {
        this.target = target;
    }

    public static Object newInstance(Object target) {
        Class targetClass = target.getClass();
        Class interfaces[] = targetClass.getInterfaces();
        return Proxy.newProxyInstance(targetClass.getClassLoader(),
                interfaces, new DynamicProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invocationResult = null;

        System.out.println("[*] Someone called " + method.getName());
        invocationResult = method.invoke(this.target, args);

        return invocationResult;
    }
}
