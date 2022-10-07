package com.ph.workflow.operator;

import com.ph.workflow.PeNode;
import com.ph.workflow.context.PeContext;
import com.ph.workflow.engine.ProcessEngine;

/**
 * 结果邮件通知
 */
public class OperatorOfNotify implements IOperator {
    @Override
    public String getType() {
        return "notify";
    }

    @Override
    public void doTask(ProcessEngine processEngine, PeNode node, PeContext peContext) {

        System.out.println(String.format("%s 提交的申请单 %s 被 %s 审批，结果为 %s",
                peContext.getValue("applicant"),
                peContext.getValue("price"),
                peContext.getValue("approver"),
                peContext.getValue("approvalResult")));

        processEngine.nodeFinished(node.onlyOneOut());
    }
}
