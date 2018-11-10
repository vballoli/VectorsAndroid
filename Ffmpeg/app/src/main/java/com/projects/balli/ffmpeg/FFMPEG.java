package com.projects.balli.ffmpeg;

import android.app.Application;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import static android.content.ContentValues.TAG;

public class FFMPEG extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: Application");
        FFmpeg ffmpeg = FFmpeg.getInstance(this);

        try {
            ffmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "onFailure: Build" );
                }

                @Override
                public void onSuccess() {
                    Log.e(TAG, "onSuccess: Build");
                }

                @Override
                public void onStart() {
                    Log.e(TAG, "onStart: Build" );
                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "onFinish: Build" );

                }
            });
        } catch (FFmpegNotSupportedException e) {
            Log.e(TAG, "onCreate: Not supported" + e);
        }
    }
}
