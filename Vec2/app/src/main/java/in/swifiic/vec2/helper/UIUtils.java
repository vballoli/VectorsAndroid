package in.swifiic.vec2.helper;

import android.content.Context;
import android.widget.Toast;

public class UIUtils {

    public static void makeToast(Context context, Toast toast,  String message, boolean isShort) {
        if (toast != null) {
            toast.cancel();
        }

        if (message != null) {
            if (isShort)
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            else
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
