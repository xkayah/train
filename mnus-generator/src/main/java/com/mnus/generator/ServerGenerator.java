package com.mnus.generator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.HashMap;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 20:49:54
 */
public class ServerGenerator {
    public static final String POM_PATH = "mnus-generator\\pom.xml";
    public static final String TO_PATH = "mnus-generator\\pom.xml";
    public static final String MODULE_NAME = "mnus-generator\\pom.xml";
    public static final String YY = "mnus-generator\\pom.xml";
    public static final String XXX = "mnus-generator\\";

    public static void main(String[] args) throws Exception {
        String generatorCfgPath = getGeneratorCfgPath();
        Document document = new SAXReader().read(XXX + generatorCfgPath);
        Node tableNode = document.selectSingleNode("//table");
        System.out.println(tableNode);
        Node tableNameNode = tableNode.selectSingleNode("@tableName");
        Node domainObjectNameNode = tableNode.selectSingleNode("@domainObjectName");
        System.out.println(tableNameNode.getText() + " ## " + domainObjectNameNode.getText());

    }

    private static String getGeneratorCfgPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(POM_PATH);
        Node node = document.selectSingleNode("//pom:configurationFile");
        return node.getText();
    }
}
