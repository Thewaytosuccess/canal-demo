package com.ph.workflow.operator;

import com.ph.workflow.context.PeContext;
import com.ph.workflow.PeNode;
import com.ph.workflow.engine.ProcessEngine;

public interface IOperator {

    //引擎可以据此来找到本算子
    String getType();

    //引擎调度本算子
    void doTask(ProcessEngine processEngine, PeNode node, PeContext peContext);
}
