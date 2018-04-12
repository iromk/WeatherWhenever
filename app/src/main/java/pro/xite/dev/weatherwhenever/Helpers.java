package pro.xite.dev.weatherwhenever;

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


}
