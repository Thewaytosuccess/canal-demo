package com.ph.workflow.operator;

import com.ph.workflow.context.PeContext;
import com.ph.workflow.PeNode;
import com.ph.workflow.engine.ProcessEngine;

/**
 * 提交申请单
 */
public class OperatorOfApprovalApply implements IOperator {

    public static int price = 500;

    @Override
    public String getType() {
        return "approvalApply";
    }

    @Override
    public void doTask(ProcessEngine processEngine, PeNode node, PeContext peContext) {
        //price每次减100
        peContext.putValue("price", price -= 100);
        peContext.putValue("applicant", "小张");

        processEngine.nodeFinished(node.onlyOneOut());
    }
}
