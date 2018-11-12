package in.swifiic.vec2.helper;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import static android.content.ContentValues.TAG;

public class FfmpegHelper {

    public static boolean run(final Context context, String command,
                           final String logMessage) {
        String[] ffmpeg_cmd = command.split(" ");
        FFmpeg fFmpeg = FFmpeg.getInstance(context);
        try {
            fFmpeg.execute(ffmpeg_cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    Log.e(TAG, "onStart: " );
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    Log.e(TAG, "onProgress: " + logMessage + " " + message );
                }

                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Log.e(TAG, "onSuccess: " + logMessage + " " + message );

                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    Log.e(TAG, "onFailure: " + logMessage + " " + message );
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }

            });
            return true;
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            return false;
        }
    }
}
