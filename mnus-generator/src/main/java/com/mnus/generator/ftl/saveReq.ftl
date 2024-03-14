package ${group}.${module}.req;
<#list typeSet as type>
<#if type='Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if type='BigDecimal'>
import java.math.BigDecimal;
</#if>
</#list>
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class ${Domain}SaveReq {
    <#list fieldList as field>
    /**
     * ${field.comment}
     */
    <#if field.javaType=='Date'>
        <#if field.type=='time'>
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type=='date'>
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
        <#else>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        </#if>
    </#if>
    <#if field.name!="id" && field.nameHump!="gmtCreate" && field.nameHump!="gmtModified">
        <#if !field.nullAble>
            <#if field.javaType=='String'>
    @NotBlank(message = "[${field.nameCn}]不能为空")
            <#else>
    @NotNull(message = "[${field.nameCn}]不能为空")
            </#if>
        </#if>
        <#if field.nameHump =='idCard'>
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message = "[${field.nameCn}]格式不正确")
        <#elseif field.nameHump == 'mobile' || field.nameHump == 'phoneNumber'|| field.nameHump == 'phoneNum'>
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "[${field.nameCn}]格式不正确")
        </#if>
    </#if>
    private ${field.javaType} ${field.nameHump};
    </#list>

    <#list fieldList as field>
    public ${field.javaType} get${field.nameBigHump}() {
        return ${field.nameHump};
    }

    public void set${field.nameBigHump}(${field.javaType} ${field.nameHump}) {
        this.${field.nameHump} = ${field.nameHump};
    }

    </#list>

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        <#list fieldList as field>
        sb.append(", ${field.nameHump}=").append(${field.nameHump});
        </#list>
        sb.append("]");
        return sb.toString();
    }
}
