package com.ph.buddy.aop.advice;

import com.ph.buddy.aop.annotation.Log;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LogAdvice {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method,
                                     @Advice.AllArguments Object[] args){
       if(method.isAnnotationPresent(Log.class)){
           System.out.println(
                   "[before] : methodName = " + method.getName()
                   + " ;args = " + Arrays.toString(args));
       }
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method,
                                    @Advice.AllArguments Object[] args,
                                    @Advice.Return Object result){
        if(method.isAnnotationPresent(Log.class)){
            System.out.println(
                    "[after] : methodName = " + method.getName()
                    + " ;args = " + Arrays.toString(args)
                    + " ;return result = "+ result);
        }

    }
}
