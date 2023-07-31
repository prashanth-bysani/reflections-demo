package com.demo.test.services;

import com.demo.test.annots.Component;
import com.demo.test.annots.Inject;

@Component
public class Service {
    @Inject(type = TestImpl1.class)
    private TestInter testImpl1;

    @Inject(type = TestImpl2.class)
    private TestInter testImpl2;

    public String callTest(String name) {
        return testImpl2.testMethod(name) + "\n"+ testImpl1.testMethod(name);
    }
}
