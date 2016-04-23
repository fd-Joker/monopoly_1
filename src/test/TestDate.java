package test;

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Joker on 4/23/16.
 */
public class TestDate {
    @Test
    public void test() {
        long sd = 86400000L;
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String sb=format.format(gc.getTime());
        System.out.println(sb);
    }
}
