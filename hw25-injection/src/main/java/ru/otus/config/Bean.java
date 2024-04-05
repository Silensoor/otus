package ru.otus.config;

import java.lang.reflect.Method;

public class Bean implements Comparable<Bean> {
    private final int order;
    private final String methodName;
    private final Method method;

    public Bean(int order, String methodName, Method method) {
        this.order = order;
        this.methodName = methodName;
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public Method getMethod() {
        return method;
    }



    @Override
    public int compareTo(Bean o) {
        return Integer.compare(this.order, o.order);
    }
}