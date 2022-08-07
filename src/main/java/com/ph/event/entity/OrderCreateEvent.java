package com.ph.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 订单创建事件
 * @author cdl
 */
@Data
@AllArgsConstructor
public class OrderCreateEvent {

    private Long orderId;
}
