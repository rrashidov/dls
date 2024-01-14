package org.roko.dls.sublock.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SublockDateUtil {

    private static final String SUBLOCK_DATEFLAG_FORMAT = "yyyyMMdd";

    public int getDateFlag(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(SUBLOCK_DATEFLAG_FORMAT);
        String date = sdf.format(d);
        return Integer.parseInt(date);
    }
}
