package us.corenetwork.core.util;

public class TimeUtils
{
    public final static int TICKS_PER_SECOND = 20;
    public final static int SECONDS_PER_MINUTE = 60;
    public final static int MINUTES_PER_HOUR = 60;

    private final static char SECOND = 's';
    private final static char MINUTE = 'm';
    private final static char HOUR = 'h';


    /**
       * Converts time string to integer
       *
       * @param arg Time, either written just as number (for example "10") or with added unit (for example "10s", "10m", "10h").
       * @return resulting time in seconds, or -1 if time string was invalid
     */
    public static int getTimeFromString(String arg)
    {
        if (Util.isInteger(arg))
            return Integer.parseInt(arg);

        char c = Character.toLowerCase(arg.charAt(arg.length() - 1));

        try
        {
            int duration = Integer.parseInt(arg.substring(0, arg.length()-1));

            switch (c) {
                case MINUTE:
                    duration = duration * SECONDS_PER_MINUTE;
                    break;
                case HOUR:
                    duration = duration * SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
                    break;
                default:
                    break;
            }

            return duration;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    /**
        Checks if input string can be parsed as time string. This function allows using just number as input.
     */
    public static boolean isTime(String arg)
    {
        if (Util.isInteger(arg))
            return true;

        return isStrictlyTime(arg);
    }

    /**
        Checks if input string can be parsed as time string. This function marks just number without unit as invalid time string.
     */
    public static boolean isStrictlyTime(String arg)
    {
        if (Util.isInteger(arg))
            return true;

        char c = Character.toLowerCase(arg.charAt(arg.length() - 1));
        String number = arg.substring(0, arg.length()-1);

        if ((c == SECOND || c == MINUTE || c == HOUR) && Util.isInteger(number))
            return true;

        return false;
    }

}
