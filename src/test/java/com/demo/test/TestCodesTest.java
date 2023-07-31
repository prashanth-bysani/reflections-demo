package com.demo.test;


import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class TestCodesTest {

    @Test
    public void testNullValue() {
        String value = null;
        Assert.assertFalse(Optional.ofNullable(value).isPresent());
    }

    @Test
    public void testNonNullValue() {
        String value = "test";
        Assert.assertTrue(Optional.ofNullable(value).isPresent());
    }
}