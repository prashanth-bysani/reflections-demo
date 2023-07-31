package com.demo.test.services;

import com.demo.test.annots.Component;

@Component
public class ServiceV2 {
    public void callOut(String key, String value) {
        System.out.println(String.format("%s --> %s", key, value));
    }
}
