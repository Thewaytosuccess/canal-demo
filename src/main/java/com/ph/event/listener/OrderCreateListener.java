package com.ph.event.listener;

import com.ph.event.entity.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author cdl
 */
@Component
@Slf4j
public class OrderCreateListener {

    @Async
    @EventListener(OrderCreateEvent.class)
    public void sendMsg(OrderCreateEvent event){
        log.info("接收到订单创建成功的通知，orderId = {}",event.getOrderId());
        log.info("开始发送消息 =============== ");
        log.info("开始增加积分 =============== ");
    }
}
