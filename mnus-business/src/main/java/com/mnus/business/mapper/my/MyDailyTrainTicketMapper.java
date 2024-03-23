package com.mnus.business.mapper.my;

import java.util.Date;

public interface MyDailyTrainTicketMapper {
    long updateCountBySell(
            Date date, String trainCode, String seatType,
            Integer minStart, Integer maxStart,
            Integer minEnd, Integer maxEnd
    );
}