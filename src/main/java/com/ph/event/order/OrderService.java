package com.ph.event.order;

import com.ph.event.entity.OrderCreateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author cdl
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderService implements ApplicationRunner {

    private ApplicationContext applicationContext;

    public void create(){
        Long orderId = ThreadLocalRandom.current().nextLong(10000000,100000000);
        log.info("开始创建订单：{}",orderId);
        applicationContext.publishEvent(new OrderCreateEvent(orderId));
    }

    @Override
    public void run(ApplicationArguments args){
        create();
    }
}
