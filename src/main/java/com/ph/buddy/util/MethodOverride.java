package com.ph.buddy.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class MethodOverride {

    @Override
    public String toString() {
        return "toString";
    }

    public static String delegate(){
        return "delegate method";
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MethodOverride original = new MethodOverride();
        System.out.println("original = " + original.toString());

        //重写父类的方法
        MethodOverride methodOverride = new ByteBuddy()
                .subclass(MethodOverride.class)
                .method(ElementMatchers.isToString())
                .intercept(FixedValue.value("override to String"))
                .make()
                .load(MethodOverride.class.getClassLoader())
                .getLoaded()
                .newInstance();
        System.out.println("result = " + methodOverride.toString());

        //执行代理方法，通过调用createOrder方法，来代理delegate方法，必须是静态方法
        Class<?> clazz = new ByteBuddy()
                .subclass(Object.class)
                .name("DynamicClass")
                .defineMethod("createOrder", String.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(MethodOverride.class))
                .defineField("e", String.class, Modifier.PUBLIC)
                .make()
                .load(MethodOverride.class.getClassLoader())
                .getLoaded();
        Method method = clazz.getDeclaredMethod("createOrder");
        System.out.println("create order = " + method.invoke(clazz.newInstance()));

        //重新定义一个已经存在的方法
        ByteBuddyAgent.install();
        new ByteBuddy().redefine(MethodOverride.class)
                .method(named("toString"))
                .intercept(FixedValue.value("redefine method of toString"))
                .make()
                .load(MethodOverride.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(new MethodOverride().toString());

    }

}
