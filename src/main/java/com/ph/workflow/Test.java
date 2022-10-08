package com.ph.workflow;

import com.ph.workflow.engine.ProcessEngine;
import com.ph.workflow.operator.OperatorOfApproval;
import com.ph.workflow.operator.OperatorOfApprovalApply;
import com.ph.workflow.operator.OperatorOfNotify;
import com.ph.workflow.operator.OperatorOfSimpleGateway;

public class Test {

    public static void main(String[] args) throws Exception {
        //读取文件内容到字符串
        ProcessEngine processEngine = new ProcessEngine("src/main/resources/workflow/workflow.xml");

        //可以采用自动注册
        processEngine.registNodeProcessor(new OperatorOfApproval());
        processEngine.registNodeProcessor(new OperatorOfApprovalApply());
        processEngine.registNodeProcessor(new OperatorOfNotify());
        processEngine.registNodeProcessor(new OperatorOfSimpleGateway());

        processEngine.start();

        Thread.sleep(1000);
    }
}
