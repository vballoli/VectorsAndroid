package in.swifiic.vec2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import in.swifiic.vec2.helper.SharedPrefsUtils;
import in.swifiic.vec2.helper.UIUtils;

import static android.content.ContentValues.TAG;

public class Vec2 extends Application {

    private Toast toast;

    private final String FIRST_TIME = "firstTime";

    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: Application class executed"  );
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(FIRST_TIME, false)) {
            Log.e(TAG, "onCreate: Build and copying only the first time" );
            transferFiles();
            buildFFMpeg();
            SharedPrefsUtils.setBooleanPreference(this, Constants.RESOLUTION_QUALITY, false);
        }
    }

    /**
     * Build FFMpeg for the device.
     */
    private void buildFFMpeg() {
        FFmpeg fFmpeg  = FFmpeg.getInstance(this);

        try {
            fFmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {
                    Log.e(TAG, "onSuccess: FFMpeg build failed ");
                }

                @Override
                public void onSuccess() {
                    Log.e(TAG, "onSuccess: FFMpeg build successful ");
                }

                @Override
                public void onFinish() { preferences.edit().putBoolean(FIRST_TIME, false).apply(); }
            });
        } catch (FFmpegNotSupportedException e) {
            UIUtils.makeToast(this, toast, "Build FFMpeg failed", true);
        }
    }

    /**
     * Copies required executable files to /data/data/in.swifiic.vec2/files/
     */
    private void transferFiles() {
        Log.e(TAG, "transferFiles: Transferring " );
        copyExecutableToAppDirectory(Constants.DOWN_CONVERT_STATIC_D.replace(Constants.filesBase, ""));
        copyExecutableToAppDirectory(Constants.EXTRACT_ADD_LS.replace(Constants.filesBase, ""));
        copyExecutableToAppDirectory(Constants.T_APP_ENCODER_STATIC_D.replace(Constants.filesBase, ""));
        copyExecutableToAppDirectory(Constants.RASCALE.replace(Constants.filesBase, ""));
        copyExecutableToAppDirectory(Constants.LAYERS2.replace(Constants.filesBase, ""));
        copyExecutableToAppDirectory(Constants._2L2X.replace(Constants.filesBase, ""));
    }

    /**
     * @param filename : Copies file from assets folder to /data/data/in.swifiic.vec2/files/ -
     *                 Private to the app
     */
    private void copyExecutableToAppDirectory(String filename) {
        try {
            InputStream ins = getAssets().open(filename);
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(buffer);
            fos.close();

            File file = getFileStreamPath (filename);
            file.setExecutable(true); // Sets executable permission to the transferred file
            Log.e(TAG, "copyExecutableToAppDirectory: Transfer Success " + filename );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "importExecutables: File " + filename + " not found error " );
            Log.e(TAG, "copyExecutableToAppDirectory: Transfer Failed: " + filename );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "importExecutables: IO exception for file: " + filename );
            Log.e(TAG, "copyExecutableToAppDirectory: Transfer Failed: " + filename );
        }
    }

}
