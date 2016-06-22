package test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Joker on 6/20/16.
 */
public class CalendarTest {
    public static void main(String[] args) {
        long sd = 86400000L;
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        gc.add(Calendar.DAY_OF_WEEK, 7);
        dat = gc.getTime();
        String s = DateFormat.getDateInstance(DateFormat.FULL).format(dat);
        System.out.println(s);
        System.out.println(gc.get(Calendar.DAY_OF_WEEK));
    }
}
