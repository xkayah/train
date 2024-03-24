package com.mnus.ucenter.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.req.TicketInsertReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.ucenter.domain.Ticket;
import com.mnus.ucenter.domain.TicketExample;
import com.mnus.ucenter.mapper.TicketMapper;
import com.mnus.ucenter.req.TicketQueryReq;
import com.mnus.ucenter.req.TicketSaveReq;
import com.mnus.ucenter.resp.TicketQueryResp;
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
public class TicketService {
    private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);
    @Resource
    private TicketMapper ticketMapper;

    public void save(TicketSaveReq req) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        // notnull,insert
        if (Objects.isNull(ticket.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            ticket.setId(IdGenUtil.nextId());
            ticket.setGmtCreate(now);
            ticket.setGmtModified(now);
            ticketMapper.insert(ticket);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            ticket.setGmtModified(now);
            ticketMapper.updateByPrimaryKeySelective(ticket);
        }
    }

    // insert 是仅插入数据, save 是插入或更新数据
    public void insert(TicketInsertReq req) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        ticket.setId(IdGenUtil.nextId());
        ticket.setGmtCreate(now);
        ticket.setGmtModified(now);
        ticketMapper.insert(ticket);
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        ticketMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<TicketQueryResp> queryList(TicketQueryReq req) {
        // Long uid = req.getUserId();
        TicketExample ticketExample = new TicketExample();
        // if (Objects.nonNull(uid)) {
        //    ticketExample.createCriteria().andUserIdEqualTo(uid);
        //}
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<Ticket> ticketList = ticketMapper.selectByExample(ticketExample);
        // 获取分页
        PageInfo<Ticket> pageInfo = new PageInfo<>(ticketList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<TicketQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), TicketQueryResp.class);
        PageResp<TicketQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}