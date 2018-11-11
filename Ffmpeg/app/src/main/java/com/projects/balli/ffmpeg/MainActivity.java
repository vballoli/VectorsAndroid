package com.projects.balli.ffmpeg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 100;

    FFmpeg ffmpeg;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ffmpeg = FFmpeg.getInstance(this);

        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    CAMERA_PERMISSION);
        } else {
            importExecutables();
            String path = "/storage/emulated/0/";
            String command = "320 240 " + path +"output_test_420.yuv 160 120 " + path + "output_test_420_Q.yuv";
            CommandResult result = Shell.SH.run("/data/data/com.projects.balli.ffmpeg/files/DownConvertStaticd " + command);
            if (result.isSuccessful()) {
                Log.e(TAG, "onCreate: Shell output " + result.getStdout() );
            } else {
                Log.e(TAG, "onCreate: Shell failed " + result.getStderr() );
            }



            String inputFile = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath() + "/test.mp4";
            String outputFile = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath() + "/output_test_420.yuv";
            Log.e(TAG, "onCreate: " + inputFile );
//            //String command = "-f v4l2 -framerate 0.25 -video_size 320x240 -i " + inputFile + " -vframes 449 " + outputFile;
            //String command = "-i " + inputFile + " -s 320x240 -r 0.25 -frames:v 449 -pix_fmt yuv420p " + outputFile;

//            try {
//                run(ffmpeg, command, "YUV420 Command");
//            } catch (FFmpegCommandAlreadyRunningException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void run(FFmpeg fFmpeg, String command, final String logMessage) throws FFmpegCommandAlreadyRunningException {
        String[] ffmpeg_cmd = command.split(" ");
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
                makeToast("Fail");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "makeToast: " + message );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Process sh = Runtime.getRuntime().exec("su", null, null);
                        OutputStream os = sh.getOutputStream();
                        os.write(("chmod 666 /dev/video0").getBytes("ASCII"));
                        os.flush();
                        os.close();
                        sh.waitFor();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }

                    File file;
                    //String inputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/input_file.mp4";
                    String inputFile = "/dev/video0";
                    String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/output_v.yuv";

                    //String command = "-f v4l2 -framerate 0.25 -video_size 320x240 -i " + inputFile + " -vframes 449 " + outputFile;
                    String command = "-i " + inputFile + " -t 30 -s 320x240 -r 0.25 -frames:v 449 " + outputFile;
                    try {
                        run(ffmpeg, command, "YUV 420");
                    } catch (FFmpegCommandAlreadyRunningException e) {
                        e.printStackTrace();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void importExecutables() {
        try {
            InputStream ins = getAssets().open("DownConvertStaticd");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            FileOutputStream fos = this.openFileOutput("DownConvertStaticd", Context.MODE_PRIVATE);
            fos.write(buffer);
            fos.close();

            File file = getFileStreamPath ("DownConvertStaticd");
            file.setExecutable(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "importExecutables: File not found error " );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "importExecutables: IO exception" );
        }
    }
}
