package com.demo.test.controllers;

import com.demo.test.annots.Controller;
import com.demo.test.annots.Inject;
import com.demo.test.annots.Mapping;
import com.demo.test.services.Service;
import com.demo.test.services.ServiceV2;

@Controller
@Mapping("/firstcontroller")
public class FirstController {

    @Inject
    private Service service;

    @Inject
    private ServiceV2 serviceV2;

    @Mapping("/endpoint/one")
    public String method1(String key, String value) {
        serviceV2.callOut(key, value);
        System.out.println("I'm response from first controller endpoint one.");
        return service.callTest(value);
    }

    @Mapping("/endpoint/two")
    public String method2() {
        System.out.println("I'm response from first controller endpoint two.");
        return service.callTest("No args");
    }

    @Mapping("/endpoint/three")
    public String method3() {
        System.out.println("I'm response from first controller endpoint three.");
        return service.callTest("No args");
    }
}
