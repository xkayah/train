<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="Mysql" targetRuntime="MyBatis3" defaultModelType="flat">

        <!-- 自动检查关键字，为关键字增加反引号 -->
        <property name="autoDelimitKeywords" value="true"/>
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!--覆盖生成XML文件-->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin"/>
        <!-- 生成的实体类添加toString()方法 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>

        <!-- 不生成注释 -->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!-- 配置数据源，需要根据自己的项目修改 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://${host}:${port}/${db_name}?serverTimezone=Asia/Shanghai"
                        userId="${user}"
                        password="${pwd}">
        </jdbcConnection>

        <!-- domain类的位置 targetProject是相对pom.xml的路径-->
        <javaModelGenerator targetProject="../${module-prefix}-${module}/src/main/java"
                            targetPackage="${group}.${module}.domain"/>

        <!-- mapper xml的位置 targetProject是相对pom.xml的路径 -->
        <sqlMapGenerator targetProject="../${module-prefix}-${module}/src/main/resources"
                         targetPackage="mapper"/>

        <!-- mapper类的位置 targetProject是相对pom.xml的路径 -->
        <javaClientGenerator targetProject="../${module-prefix}-${module}/src/main/java"
                             targetPackage="${group}.${module}.mapper"
                             type="XMLMAPPER"/>

        <!-- 填写表名 -->
        <table tableName="table1" domainObjectName="Table1"/>
        <!--<table tableName="table2" domainObjectName="Table2"/>-->
        <!--<table tableName="table3" domainObjectName="Table3"/>-->
    </context>
</generatorConfiguration>
