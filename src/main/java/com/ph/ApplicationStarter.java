package com.ph;

import com.ph.dynamic.DynamicBeanLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author cdl
 */
@EnableAsync
//@SpringBootApplication
//@Import(DynamicBeanLoader.class)
public class ApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
