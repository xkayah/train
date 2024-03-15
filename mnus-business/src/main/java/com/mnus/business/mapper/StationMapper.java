package com.mnus.business.mapper;

import com.mnus.business.domain.Station;
import com.mnus.business.domain.StationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationMapper {
    long countByExample(StationExample example);

    int deleteByExample(StationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Station record);

    int insertSelective(Station record);

    List<Station> selectByExample(StationExample example);

    Station selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Station record, @Param("example") StationExample example);

    int updateByExample(@Param("record") Station record, @Param("example") StationExample example);

    int updateByPrimaryKeySelective(Station record);

    int updateByPrimaryKey(Station record);
}