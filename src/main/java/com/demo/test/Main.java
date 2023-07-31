package com.demo.test;

import com.demo.test.annots.Inject;
import com.demo.test.helper.MappingUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    public static void main(String[] args)
            throws Exception {
        String packageName = "com.demo.test";
        MappingUtils utils = new MappingUtils(packageName);
        System.out.println(utils.executeRequest("/firstcontroller/endpoint/one", "say", "world"));
        System.out.println();
        System.out.println(utils.executeRequest("/firstcontroller/endpoint/three"));
    }
    
    private static List<Class<?>> getClassesByNames(Set<String> classNames) {
        List<Class<?>> classes = new ArrayList<>();
        for(String className: classNames) {
            try {
                classes.add(Class.forName(className));
            } catch (Exception e) {
                System.out.println(String.format("Class %s does not exist", classes));
            }
        }
        return classes;
    }

    private static void executeService(Class<?> serviceClassTemp) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if(Objects.nonNull(serviceClassTemp)) {
            Class serviceClass = serviceClassTemp;
            Object instance = serviceClass.getConstructor().newInstance();
            Arrays.stream(serviceClass.getDeclaredFields()).filter(field -> Objects.nonNull(field.getAnnotation(Inject.class))).forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.set(instance, field.getAnnotation(Inject.class).type().getConstructor().newInstance());
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println(serviceClass.getDeclaredMethod("callTest", String.class).invoke(instance, "world"));
        }
    }

    private static List<Class<?>> getClassesInPackage(String packageName) {
            String packagePath = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL packageUrl = classLoader.getResource(packagePath);

            if (packageUrl == null) {
                return Collections.emptyList();
            }

            List<Class<?>> classes = new ArrayList<>();

            File packageDir = new File(packageUrl.getFile());

            if (packageDir.exists() && packageDir.isDirectory()) {
                File[] files = packageDir.listFiles();

                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName();
                        if (fileName.endsWith(".class")) {
                                String className = (!packageName.isBlank() ? packageName + '.' : "")+ fileName.substring(0, fileName.length() - 6);
                            try {
                                Class<?> cls = Class.forName(className);
                                classes.add(cls);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            return classes;
        }


}