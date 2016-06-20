package test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Joker on 6/20/16.
 */
public class CalendarTest {
    public static void main(String[] args) {
        long sd = 86400000L * 3;
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String sb=format.format(gc.getTime());
        System.out.println(sb);
        System.out.println(gc.get(Calendar.MONTH));
    }
}
