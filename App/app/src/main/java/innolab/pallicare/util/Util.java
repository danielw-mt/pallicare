package innolab.pallicare.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Contains multiple class independent methods, which are or could be used multiple times in different classes in the app.
 *
 * @author Klaus Schmidt
 */
public class Util {

    /**
     * Makes the phone vibrate for a default time of 500ms.
     *
     * @param context just use getApplicationContext to provide a suitable context.
     * @author Klaus Schmidt
     */
    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}
