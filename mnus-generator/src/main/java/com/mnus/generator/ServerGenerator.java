package com.mnus.generator;

import cn.hutool.json.JSONUtil;
import com.mnus.generator.utils.DBUtil;
import com.mnus.generator.utils.FieldDB;
import com.mnus.generator.utils.FreeMarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 20:49:54
 */
public class ServerGenerator {
    public static final String GENERATOR_BASE_PATH = "mnus-generator\\";
    public static final String GROUP = "com.mnus";
    public static final String POM_PATH = "mnus-generator\\pom.xml";
    public static final String FTL_TMP = "${target}.ftl";
    public static final String FILE_PATH_TMP = "mnus-${module}/src/main/java/com/mnus/${module}/${pkg}/";
    public static final String FILE_NAME_TMP = "${Domain}${Pkg}.java";
    public static final String $_target = "${target}";
    public static final String $_module = "${module}";
    public static final String $_pkg = "${pkg}";
    public static final String $_Pkg = "${Pkg}";
    public static final String $_Domain = "${Domain}";
    public static final String REQ_PKG = "req";
    public static final String RESP_PKG = "resp";
    public static final String REQ_SUFFIX = "Req";
    public static final String RESP_SUFFIX = "Resp";

    public static void main(String[] args) throws Exception {
        String generatorCfgPath = getGeneratorCfgPath();
        Document document = new SAXReader().read(GENERATOR_BASE_PATH + generatorCfgPath);
        /* Node tableNode = document.selectSingleNode("//table");
        Node tableNameNode = tableNode.selectSingleNode("@tableName");
        Node domainObjectNameNode = tableNode.selectSingleNode("@domainObjectName");
        String tableName = tableNameNode.getText();
        String domainObjectName = domainObjectNameNode.getText(); */
        // 读 table 节点的表名和实体名
        String tableName = document.selectSingleNode("//@tableName").getText();
        String domainObjectName = document.selectSingleNode("//@domainObjectName").getText();
        System.out.println(String.format("[tableName]:%s,[domainObjectName]:%s", tableName, domainObjectName));

        // 读 DB 配置信息
        String connectionURL = document.selectSingleNode("//@connectionURL").getText();
        String userId = document.selectSingleNode("//@userId").getText();
        String password = document.selectSingleNode("//@password").getText();
        DBUtil.url = connectionURL;
        DBUtil.username = userId;
        DBUtil.password = password;
        System.out.println(String.format("url:%s,user:%s,pwd:%s",
                connectionURL, userId, password));

        // tableName="table_name" domainObjectName="TableName"
        // Domain = TableName
        // domain = tableName
        // do_main = table-name
        String Domain = domainObjectName;
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        String do_main = tableName.replace("_", "-");

        // 模块名
        String module = getModuleName(generatorCfgPath);

        // 获取表中的字段信息
        List<FieldDB> fieldDBList = DBUtil.getColumnByTableName(tableName);
        Set<String> javaTypes = getJavaTypes(fieldDBList);

        // 组装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("group", GROUP);
        map.put("Domain", Domain);
        map.put("domain", domain);
        map.put("do_main", do_main);
        map.put("module", module);
        map.put("typeSet", javaTypes);
        map.put("fieldList", fieldDBList);

        // 执行
        // gen("service", Domain, map);
        // gen("controller", Domain, map);
        // gen("saveReq", Domain, map);
        // gen("queryReq", Domain, map);
        gen("queryResp", Domain, map);


    }

    public static void gen(String target, String Domain, Map<String, Object> map) throws Exception {
        String module = getModuleName(getGeneratorCfgPath());
        String path = genToPath(module, target, Domain);
        System.out.println(String.format("[module]:%s,[path]:%s\n[params]:%s",
                module, path, map));
        FreeMarkerUtil.initConfig(FTL_TMP.replace($_target, target));
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
        // pkg -> Pkg
        String fileName = FILE_NAME_TMP.
                replace($_Pkg, pkg.substring(0, 1).toUpperCase() + pkg.substring(1)).
                replace($_Domain, Domain);
        // special case: saveReq -> req
        if (pkg.contains(REQ_SUFFIX)) {
            pkg = REQ_PKG;
        } else if (pkg.contains(RESP_SUFFIX)) {
            pkg = RESP_PKG;
        }
        String filePath = FILE_PATH_TMP.
                replace($_module, module).
                replace($_pkg, pkg);
        new File(filePath).mkdirs();
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

    /**
     * 获取所有Java类型
     *
     * @param list 字段信息列表
     * @return
     */
    private static Set<String> getJavaTypes(List<FieldDB> list) {
        HashSet<String> set = new HashSet<>();
        for (FieldDB fieldDB : list) {
            set.add(fieldDB.getJavaType());
        }
        return set;
    }
}
