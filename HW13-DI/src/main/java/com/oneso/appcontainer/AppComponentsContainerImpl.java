package com.oneso.appcontainer;

import com.oneso.appcontainer.api.AppComponent;
import com.oneso.appcontainer.api.AppComponentsContainer;
import com.oneso.appcontainer.api.AppComponentsContainerConfig;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(String packet) {
        processConfig(packet);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var reflection = new Reflections(configClass, new MethodAnnotationsScanner());
        List<Method> methods = reflection.getMethodsAnnotatedWith(AppComponent.class).stream()
                .filter(filter -> filter.getDeclaringClass().equals(configClass))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        for (Method method : methods) {
            initComponent(method, configClass);
        }
    }

    private void processConfig(Class<?>... configClasses) {
        List<Method> methods = new ArrayList<>();

        for (var aClass : configClasses) {
            checkConfigClass(aClass);
            var reflection = new Reflections(configClasses, new MethodAnnotationsScanner());
            methods.addAll(reflection.getMethodsAnnotatedWith(AppComponent.class).stream()
                    .filter(filter -> filter.getDeclaringClass().equals(aClass))
                    .collect(Collectors.toList()));
        }

        List<Method> sortedMethod = methods.stream()
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        for (var temp : sortedMethod) {
            initComponent(temp, temp.getDeclaringClass());
        }
    }

    private void processConfig(String packet) {
        var reflections = new Reflections(packet, new TypeAnnotationsScanner(), new SubTypesScanner());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);

        processConfig(classes.toArray(new Class<?>[0]));
    }

    private void initComponent(Method method, Class<?> configClass) {
        try {
            Object instance = configClass.getConstructors()[0].newInstance();
            String name = method.getAnnotation(AppComponent.class).name();
            Object component;

            if (method.getParameters().length >= 1) {
                List<Object> argForMethod = new ArrayList<>();
                Class<?>[] parameterTypes = method.getParameterTypes();

                for (var parameter : parameterTypes) {
                    for (var temp : appComponents) {
                        if (parameter.isAssignableFrom(temp.getClass())) {
                            argForMethod.add(temp);
                        }
                    }
                }

                component = method.invoke(instance, argForMethod.toArray());

            } else {
                component = method.invoke(instance);
            }

            appComponents.add(component);
            appComponentsByName.put(name, component);

        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Cannot initialize components");
            e.printStackTrace();
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (var component : appComponents) {
            if (component.equals(componentClass)) {
                return componentClass.cast(component);
            }
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        for (var component : appComponentsByName.entrySet()) {
            if (component.getKey().equalsIgnoreCase(componentName)) {
                return (C) component.getValue();
            }
        }
        return null;
    }
}
