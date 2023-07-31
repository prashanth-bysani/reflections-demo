package com.demo.test.helper;

import com.demo.test.annots.Component;
import com.demo.test.annots.Controller;
import com.demo.test.annots.Inject;
import com.demo.test.annots.Mapping;
import com.demo.test.entity.MappingHolder;
import com.demo.test.services.Service;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class MappingUtils {

    private Service ss;
    public MappingUtils(String packageName) {
        initialize(packageName);
    }
    private void initialize(String packageName) {
        Reflections reflections= new Reflections(packageName, new SubTypesScanner(false), new TypeAnnotationsScanner());
        TreeMap<String, Object> componentInstances = new TreeMap<>();
        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        components.addAll(controllers);
        components.stream().filter(aClass -> !aClass.isAnnotation()).forEach(aClass -> {
            try {
                componentInstances.put(aClass.getName(), aClass.getConstructor().newInstance());
            } catch (Exception exception) {
                System.out.println(String.format("Cannot create instance of %s", aClass.getName()));
            }
        });
        components.forEach(aClass -> {
            injectValues(componentInstances, aClass);
        });
        controllers.forEach(controller -> {
            ReflectionUtils.getAllMethods(controller, method -> Objects.nonNull(method.getAnnotation(Mapping.class)))
                    .forEach(method -> {
                        String mapping = (Objects.nonNull(controller.getAnnotation(Mapping.class))
                                ? controller.getAnnotation(Mapping.class).value() : "")
                                + method.getAnnotation(Mapping.class).value();
                        MappingHolder.addMapping(mapping,
                                new MappingHolder.MappingData(componentInstances.get(controller.getName())
                                        , method, Arrays.asList(method.getParameters())));
                    });
        });
    }

    private static void injectValues(TreeMap<String, Object> componentInstances, Class<?> aClass) {
        ReflectionUtils.getAllFields(aClass, field -> Objects.nonNull(field.getAnnotation(Inject.class))).forEach(field -> {
            try {
                field.setAccessible(true);
                String type = "Void".equals(field.getAnnotation(Inject.class).type().getSimpleName()) ?
                        field.getType().getName() : field.getAnnotation(Inject.class).type().getName();
                if (componentInstances.containsKey(type)) {
                    field.set(componentInstances.get(aClass.getName()), componentInstances.get(type));
                }
            } catch (Exception e){
                System.out.println(String.format("Cannot inject value to the field %s for instance %s",
                        field.getType().getName(), aClass.getName()));
            }
        });
    }

    public Object executeRequest(String mapping, Object... args) throws Exception {
        MappingHolder.MappingData data = MappingHolder.getMappingMethod(mapping);
        if(Objects.isNull(data))
            throw new Exception("Mapping not found");
        else if (!data.getParameters().isEmpty() && !data.getParameters().stream().map(Parameter::getType)
                .map(Class::getName).collect(Collectors.joining())
                .equals(Arrays.stream(args).map(arg -> arg.getClass().getName()).collect(Collectors.joining()))) {
            throw new Exception("Mapping found but argument mismatch");
        }
        try {
           return data.getMethod().invoke(data.getInstance(), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new Exception("Cannot execute:"+e.getMessage());
        }
    }
}
