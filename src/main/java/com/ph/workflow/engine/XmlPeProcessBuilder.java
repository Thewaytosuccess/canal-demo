package com.ph.workflow.engine;

import com.ph.workflow.PeEdge;
import com.ph.workflow.PeNode;
import com.ph.workflow.PeProcess;
import com.ph.workflow.util.XmlUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlPeProcessBuilder {
    private String xmlStr;
    private final Map<String, PeNode> id2PeNode = new HashMap<>();
    private final Map<String, PeEdge> id2PeEdge = new HashMap<>();

    public XmlPeProcessBuilder(String xmlStr) {
        this.xmlStr = xmlStr;
    }

    public PeProcess build(){
        XmlUtil.parse(xmlStr);

        Node process = XmlUtil.getNodeByName("process");
        if(Objects.isNull(process)){
            throw new IllegalStateException("can not find node : process");
        }

        NodeList childNodes = process.getChildNodes();

        for (int j = 0; j < childNodes.getLength(); j++) {
            Node node = childNodes.item(j);
            if (node.getNodeType() == Node.TEXT_NODE) continue;

            if ("sequenceFlow".equals(node.getNodeName()))
                buildPeEdge(node);
            else
                buildPeNode(node);
        }

        Map.Entry<String, PeNode> startEventEntry = id2PeNode.entrySet().stream()
                .filter(entry -> "startEvent".equals(entry.getValue().type)).findFirst().get();
        return new PeProcess(startEventEntry.getKey(), startEventEntry.getValue());
    }

    private void buildPeEdge(Node node) {
        //attributeValue : 找到node节点上属性为id的值
        PeEdge peEdge = id2PeEdge.computeIfAbsent(XmlUtil.attributeValue(node, "id"), PeEdge::new);
        peEdge.from = id2PeNode.computeIfAbsent(XmlUtil.attributeValue(node, "sourceRef"), PeNode::new);
        peEdge.to = id2PeNode.computeIfAbsent(XmlUtil.attributeValue(node, "targetRef"), PeNode::new);
    }

    private void buildPeNode(Node node) {
        PeNode peNode = id2PeNode.computeIfAbsent(XmlUtil.attributeValue(node, "id"), PeNode::new);
        peNode.type = node.getNodeName();
        peNode.xmlNode = node;

        List<Node> inPeEdgeNodes = XmlUtil.childrenByName(node, "incoming");
        inPeEdgeNodes.forEach(e -> peNode.in.add(id2PeEdge.computeIfAbsent(
                XmlUtil.text(e), PeEdge::new)));

        List<Node> outPeEdgeNodes = XmlUtil.childrenByName(node, "outgoing");
        outPeEdgeNodes.forEach(e -> peNode.out.add(id2PeEdge.computeIfAbsent(XmlUtil.text(e), PeEdge::new)));
    }
}
