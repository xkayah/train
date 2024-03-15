package com.mnus.business.mapper;

import com.mnus.business.domain.DailyTrainSeat;
import com.mnus.business.domain.DailyTrainSeatExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyTrainSeatMapper {
    long countByExample(DailyTrainSeatExample example);

    int deleteByExample(DailyTrainSeatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrainSeat record);

    int insertSelective(DailyTrainSeat record);

    List<DailyTrainSeat> selectByExample(DailyTrainSeatExample example);

    DailyTrainSeat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DailyTrainSeat record, @Param("example") DailyTrainSeatExample example);

    int updateByExample(@Param("record") DailyTrainSeat record, @Param("example") DailyTrainSeatExample example);

    int updateByPrimaryKeySelective(DailyTrainSeat record);

    int updateByPrimaryKey(DailyTrainSeat record);
}