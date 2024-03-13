package com.mnus.generator;

import com.mnus.generator.utils.FreeMarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 20:49:54
 */
public class ServerGenerator {
    public static final String GENERATOR_BASE_PATH = "mnus-generator\\";
    public static final String MODULE_PREFIX = "mnus_";
    public static final String POM_PATH = "mnus-generator\\pom.xml";
    public static final String SERVICE_PATH = "mnus-${module}/src/main/java/com/mnus/${module}/service/${Domain}Service.java";
    public static final String CONTROLLER_PATH = "mnus-${module}/src/main/java/com/mnus/${module}/controller";
    public static final String REQ_PATH = "mnus-${module}/src/main/java/com/mnus/${module}/req";
    public static final String RESP_PATH = "mnus-${module}/src/main/java/com/mnus/${module}/resp";
    public static final String VUE_PATH = "mnus-generator\\pom.xml";

    public static void main(String[] args) throws Exception {
        String generatorCfgPath = getGeneratorCfgPath();
        Document document = new SAXReader().read(GENERATOR_BASE_PATH + generatorCfgPath);
        Node tableNode = document.selectSingleNode("//table");
        // 读 table 节点的表名和实体名
        Node tableNameNode = tableNode.selectSingleNode("@tableName");
        Node domainObjectNameNode = tableNode.selectSingleNode("@domainObjectName");
        String tableName = tableNameNode.getText();
        String domainObjectName = domainObjectNameNode.getText();
        System.out.println(String.format("[tableName]:%s,[domainObjectName]:%s", tableName, domainObjectName));

        // tableName="table_name" domainObjectName="TableName"
        // Domain = TableName
        // domain = tableName
        // do_main = table-name
        String Domain = domainObjectName;
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        String do_main = tableName.replace("_", "-");

        // 组装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("Domain", Domain);
        map.put("domain", domain);
        map.put("do_main", do_main);

        gen("service", Domain, map);

    }

    public static void gen(String target, String Domain, Map<String, Object> map) throws Exception {
        String module = getModuleName(getGeneratorCfgPath());
        String path = genToPath(module, target, Domain);
        System.out.println(String.format("[module]:%s,[path]:%s", module, path));
        FreeMarkerUtil.initConfig("service.ftl");
        FreeMarkerUtil.gen(path, map);

    }

    /**
     * 获取模块名
     *
     * @param generatorCfgPath 生成器配置文件的路径
     * @return
     */
    public static String getModuleName(String generatorCfgPath) {
        return generatorCfgPath.
                replace("src/main/resources/generator-config-", "").
                replace(".xml", "");
    }

    /**
     * 获取生成路径并创建文件夹
     *
     * @param module 模块名
     * @param pkg    包名
     * @param Domain 实体名
     * @return
     */
    private static String genToPath(String module, String pkg, String Domain) {
        String filePathTmp = "mnus-${module}/src/main/java/com/mnus/${module}/${pkg}/";
        String fileNameTmp = "${Domain}${Pkg}.java";
        String filePath = filePathTmp.
                replace("${module}", module).
                replace("${pkg}", pkg);
        new File(filePath).mkdirs();
        String fileName = fileNameTmp.
                replace("${Pkg}", pkg.substring(0, 1).toUpperCase() + pkg.substring(1)).
                replace("${Domain}", Domain);
        return filePath + fileName;
    }

    /**
     * 获取生成器配置文件路径，通过POM文件读取
     *
     * @return
     * @throws DocumentException
     */
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
