package com.ph.workflow.util;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* DOM方式解析xml
*/
public class XmlUtil {

    private static Document document;

    public static void parse(String xml){
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Node getNodeByName(String tag){
        try {
            NodeList nodes = document.getElementsByTagName(tag);
            for (int i = 0; i < nodes.getLength(); i++) {
                if(nodes.item(i).getNodeName().equals(tag)){
                    return nodes.item(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String text(Node node){
        NodeList childNodes = node.getChildNodes();
        for (int k = 0; k < childNodes.getLength(); k++) {
            if(Objects.nonNull(childNodes.item(k).getNodeValue())){
                return childNodes.item(k).getNodeValue();
            }
        }
        return null;
    }


    public static String attributeValue(Node node,String tag){
        NamedNodeMap attrs = node.getAttributes();
        for (int j = 0; j < attrs.getLength(); j++) {
            Node attr = attrs.item(j);
            if(attr.getNodeName().equals(tag)){
                return attr.getNodeValue();
            }
        }
        return null;
    }

    public static List<Node> childrenByName(Node node,String tag){
        List<Node> list = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int k = 0; k < childNodes.getLength(); k++) {
            if(childNodes.item(k).getNodeType() == Node.ELEMENT_NODE){
                if(childNodes.item(k).getNodeName().equals(tag)){
                    list.add(childNodes.item(k));
                }
            }
        }
        return list;
    }

    public static String childTextByName(Node node,String tag){
        NodeList childNodes = node.getChildNodes();
        for (int k = 0; k < childNodes.getLength(); k++) {
            if(childNodes.item(k).getNodeType() == Node.ELEMENT_NODE){
                if(childNodes.item(k).getNodeName().equals(tag)){
                    return childNodes.item(k).getFirstChild().getNodeValue();
                }
            }
        }
        return null;
    }
}
