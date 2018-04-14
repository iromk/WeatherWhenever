package pro.xite.dev.weatherwhenever;

import java.util.Calendar;
import java.util.TimeZone;

import pro.xite.dev.weatherwhenever.data.Wherever;

/**
 * Created by Roman Syrchin on 3/25/18.
 */

public class Helpers {

    /**
     * Get the method name from stack trace.
     * @param depth int >= 0
     * if depth == 0 is this helper method getMethodName(int)
     * depth == 1 method that invoked helper method
     * @return method name as String
     */
    public static String getMethodName(final int depth)
    {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[2 + depth].getMethodName();
    }

    /**
     * Return method name with depth = 2, e.g. method which has called getMethodName()
     * @return method name
     */
    public static String getMethodName() {
        return getMethodName(2);
    }

    public static String tempToString(float tFloat) {
        final int tInt = (int)Math.abs(tFloat);
        String tPrefix = " ";
        if(tInt > 0 ) tPrefix = "+";
        if(tInt < 0 ) tPrefix = "-";
        final String tIntStr = String.valueOf(tInt);
        return String.format("%s%s", tPrefix, tIntStr);
    }


    public static Calendar nextAfternoon(int daysShift) {
        Calendar now = Calendar.getInstance();//wherever.getTimezone());
        long msNow = now.getTimeInMillis();
        long msShift = daysShift * 1_000 * 60 * 60 * 24;
        now.setTimeInMillis(msNow + msShift);
        now.set(Calendar.HOUR_OF_DAY, 13);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }
}
