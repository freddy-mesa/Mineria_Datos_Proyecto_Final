package GUI.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

public class CalendarUtil {

    /**
     * Default date format in the form 2013-03-18.
     */
    private static final SimpleDateFormat DATE_FORMAT_BIRTHDAY = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");

    /**
     * Returns the given date as a well formatted string. The above defined date
     * format is used.
     *
     * @param calendar date to be returned as a string
     * @param isBirthday if it's a birthday date
     * @return formatted string
     */
    public static String format(Calendar calendar, boolean isBirthday) {
        if (calendar == null) {
            return null;
        }
        if(isBirthday)
            return DATE_FORMAT_BIRTHDAY.format(calendar.getTime());
        else
            return DATE_FORMAT.format(calendar.getTime());
    }

    /**
     * Converts a String in the format "yyyy-MM-dd" to a Calendar object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @param isBirthday if it's a birthday date
     * @return the calendar object or null if it could not be converted
     */
    public static Calendar parse(String dateString, boolean isBirthday) {
        Calendar result = Calendar.getInstance();
        try {
            if(isBirthday)
                result.setTime(DATE_FORMAT_BIRTHDAY.parse(dateString));
            else
                result.setTime(DATE_FORMAT.parse(dateString));
        } catch (ParseException e) {
            return null;
        }

        return result;
    }

    /**
     * Checks the String whether it is a valid date.
     *
     * @param dateString
     * @param isBirthday if it's a birthday date
     * @return true if the String is a valid date
     */
    public static boolean validString(String dateString, boolean isBirthday) {
        try {
            if(isBirthday)
                DATE_FORMAT_BIRTHDAY.parse(dateString);
            else
                DATE_FORMAT.parse(dateString);

        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static int getAge(String dateString, boolean isBirthday){
        Calendar dob = parse(dateString,isBirthday);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }
}
