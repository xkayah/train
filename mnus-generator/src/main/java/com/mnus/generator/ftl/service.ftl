package ${group}.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${group}.common.context.ReqHolder;
import ${group}.common.enums.BaseErrorCodeEnum;
import ${group}.common.exception.BizException;
import ${group}.common.req.EntityDeleteReq;
import ${group}.common.resp.PageResp;
import ${group}.common.utils.IdGenUtil;
import ${group}.${module}.domain.${Domain};
import ${group}.${module}.domain.${Domain}Example;
import ${group}.${module}.mapper.${Domain}Mapper;
import ${group}.${module}.req.${Domain}QueryReq;
import ${group}.${module}.req.${Domain}SaveReq;
import ${group}.${module}.resp.${Domain}QueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class ${Domain}Service {
    private static final Logger LOG = LoggerFactory.getLogger(${Domain}Service.class);
    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveReq req) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);
        // notnull,insert
        if (Objects.isNull(${domain}.getId())) {
            ${domain}.setId(IdGenUtil.nextId());
            ${domain}.setGmtCreate(now);
            ${domain}.setGmtModified(now);
            ${domain}Mapper.insert(${domain});
        } else {
            // null,update
            if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
                throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            }
            ${domain}.setGmtModified(now);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        ${domain}Mapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req) {
        Long uid = req.getUserId();
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        if (Objects.nonNull(uid)) {
            ${domain}Example.createCriteria().andUserIdEqualTo(uid);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);
        // 获取分页
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<${Domain}QueryResp> list = BeanUtil.copyToList(pageInfo.getList(), ${Domain}QueryResp.class);
        PageResp<${Domain}QueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}