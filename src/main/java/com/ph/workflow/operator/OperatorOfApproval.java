package com.ph.workflow.operator;

import com.ph.workflow.PeNode;
import com.ph.workflow.context.PeContext;
import com.ph.workflow.engine.ProcessEngine;

/**
 * 审批
 */
public class OperatorOfApproval implements IOperator {
    @Override
    public String getType() {
        return "approval";
    }

    @Override
    public void doTask(ProcessEngine processEngine, PeNode node, PeContext peContext) {
        peContext.putValue("approver", "经理");

        Integer price = (Integer) peContext.getValue("price");
        //价格<=200审批才通过，即：approvalResult=true
        boolean approvalResult = price <= 200;
        peContext.putValue("approvalResult", approvalResult);

        System.out.println("approvalResult ：" + approvalResult + "，price : " + price);

        processEngine.nodeFinished(node.onlyOneOut());
    }
}
