package com.demo.test.entity;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.TreeMap;

public class MappingHolder {

    private static TreeMap<String, MappingData> mappings = new TreeMap<>();

    public static MappingData getMappingMethod(String mapping) {
        return mappings.get(mapping);
    }

    public static void addMapping(String mapping, MappingData data){
        mappings.put(mapping, data);
    }


    public static class MappingData {

        private Object instance;
        private Method method;
        private List<Parameter> parameters;
        private Class<?> returnType;

        public Method getMethod () {
            return method;
        }

        public List<Parameter> getParameters () {
            return parameters;
        }

        public Class<?> getReturnType () {
            return returnType;
        }

        public Object getInstance() {
            return instance;
        }

        public MappingData(Object instance, Method method, List<Parameter> parameters){
            this.method = method;
            this.instance = instance;
            this.parameters = parameters;
        }
    }
}
