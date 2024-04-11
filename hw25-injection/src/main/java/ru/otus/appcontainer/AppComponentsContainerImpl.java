package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.Bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var beanList = Arrays.stream(configClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(AppComponent.class)).map(method -> {
            var annotation = method.getAnnotation(AppComponent.class);
            return new Bean(annotation.order(), annotation.name(), method);
        }).sorted().toList();


        Object classObj = getObject(configClass);

        for (Bean bean : beanList) {
            if (appComponentsByName.get(bean.getMethodName()) != null) {
                throw new RuntimeException("Bean %s exist.".formatted(bean.getMethodName()));
            }
            Object obj = createBean(bean.getMethod(), classObj);
            appComponentsByName.put(bean.getMethodName(), obj);
            appComponents.add(obj);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) getBean(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (Objects.isNull(component)) {
            throw new RuntimeException("Not found component %s".formatted(componentName));
        }
        return component;
    }

    private Object createBean(Method method, Object config) {
        var paramTypes = method.getParameterTypes();

        Object[] args = Arrays.stream(paramTypes).map(this::getBean).toArray();

        try {
            return method.invoke(config, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getBean(Class<?> clazz) {
        var beanList = appComponents.stream().filter(c -> c.getClass().equals(clazz) ||
                Arrays.asList(c.getClass().getInterfaces()).contains(clazz)).toList();
        if (beanList.size() > 1) {
            throw new RuntimeException("Class has more 1 bean with name ");
        }
        return beanList.get(0);
    }

    private static Object getObject(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
