package innolab.pallicare.db.converters;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Type Converters for database, for conversion of SQLite compatible data types to Java data types and vice versa
 *
 * @author Ulla Sternemann
 */
public class Converters {

    private static final String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";

    @TypeConverter
    public static Date longTimestampToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long dateToLongTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Convert the given date to a String in the format yyyy-MM-dd HH:mm:ss
     * @param date any Date
     * @return a string of  yyyy-MM-dd HH:mm:ss
     */
    public static String dateToStringAPI(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_8, Locale.GERMAN);
        return dateFormat.format(date);
    }
}
