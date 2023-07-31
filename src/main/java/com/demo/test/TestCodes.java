package com.demo.test;

import java.util.Optional;

public class TestCodes {
    public static void main(String[] args) {
//        Pass a non-null value to Optional and validate the result
//        Pass a null value to Optional and validate the result
        validateNull("test");
        validateNull(null);


        // Pass a null value to Optional and make sure it won’t get NullPointerException
        checkNullAndPrint(null);
        checkNullAndPrint("test");

        // Create an Optional object with empty value and validate the result
        Optional<Object> emptyValue=Optional.empty();
        if(emptyValue.isEmpty())
            System.out.println("Value is empty");
        else
            System.out.println("Value is "+emptyValue.get());

    }

    private static void validateNull(String value) {
        Optional<String> opValue  = Optional.ofNullable(value);
        if(opValue.map(v->v.equals(value)).orElse(false))
            System.out.println("Values are same");
        else
            System.out.println("Values are not same");
    }

    // Pass a null value to Optional and make sure it won’t get NullPointerException
    private static void checkNullAndPrint(String nullValue) {
        Integer stringLength = Optional.ofNullable(nullValue).map(String::length).orElse(0);
        System.out.println(String.format("Length of string %s is %d", nullValue, stringLength));
    }

    // Write code to have a conditional value existence check like above and write a conditional code of action if value exists in Optional
    public static void conditionalCheck(String x) {
        Optional<String> b = Optional.ofNullable(x);
        if (b.isPresent()) {
            String c = b.get();
            System.out.println("Validate Value: " + c);
        } else {
            System.out.println("Value is null.");
        }
    }
}
