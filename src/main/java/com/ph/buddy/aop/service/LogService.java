package com.ph.buddy.aop.service;

import com.ph.buddy.aop.advice.LogAdvice;
import com.ph.buddy.aop.annotation.Log;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class LogService {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        LogService logService = new ByteBuddy()
                //动态生成指定类的子类
                .subclass(LogService.class)
                //需要拦截的方法
                .method(ElementMatchers.any())
                //指定切面
                .intercept(Advice.to(LogAdvice.class))
                .make()
                .load(LogService.class.getClassLoader())
                .getLoaded()
                .newInstance();
        logService.bar(1);
        logService.foo(2);
    }

    @Log
    public int foo(int value){
        System.out.println("foo:" + value);
        return value;
    }

    public int bar(int value){
        System.out.println("bar:" + value);
        return value;
    }
}
