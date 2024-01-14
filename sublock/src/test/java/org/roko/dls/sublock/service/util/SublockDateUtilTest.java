package org.roko.dls.sublock.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SublockDateUtilTest {

    private static final String SUBLOCK_DATEFLAG_FORMAT = "yyyyMMdd";

    private SublockDateUtil util;

    @BeforeEach
    public void setup(){
        util = new SublockDateUtil();
    }

    @Test
    public void getDateFlagReturnsProperValue(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(SUBLOCK_DATEFLAG_FORMAT);
        String date = sdf.format(d);
        int expectedDateFlag = Integer.parseInt(date);

        assertEquals(expectedDateFlag, util.getDateFlag());
    }
}
