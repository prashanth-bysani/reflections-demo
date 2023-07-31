package com.demo.test.services;

import com.demo.test.annots.Component;

@Component
public class TestImpl1 implements TestInter{
    @Override
    public String testMethod(String name) {
        return String.format("Hello %s", name);
    }
}
