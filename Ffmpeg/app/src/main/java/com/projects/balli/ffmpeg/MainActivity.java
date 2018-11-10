package com.projects.balli.ffmpeg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 100;

    FFmpeg ffmpeg;

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
            String localPath = this.getFilesDir().getAbsolutePath();
            Log.e(TAG, "onCreate: Local path" + localPath );
            for (File f : File.) {
                Log.e(TAG, "Files: " + f.getName());
                Log.e(TAG, "Can exec: " + f.canExecute() );
            }

            CommandResult result = Shell.SH.run("/data/local/tmp/Vectors/SHM-12.4/bin/TAppEncoderStaticd -c");
            if (result.isSuccessful()) {
                Log.e(TAG, "onCreate: Result " + result.getStdout() );
            } else {
                Log.e(TAG, "onCreate: Unsuccess " );
                Log.e(TAG, "onCreate: Error " + result.getStderr() );
            }

//            try{
//                StringBuffer output = new StringBuffer();
//                Process p = Runtime.getRuntime().exec("ls " + localPath);
//                p.waitFor();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = "";
//                while ((line = reader.readLine())!= null) {
//                    output.append(line + "n");
//                }
//                Log.e(TAG, "onCreate: output" + output.toString() );
//            }catch(IOException | InterruptedException e){
//                Log.e(TAG, "onCreate: Process error " + e.getMessage() );
//            }

//            try {
//                Log.e(TAG, "onCreate: Running copy stuff" );
//                Process sh = Runtime.getRuntime().exec("cp -r /sdcard/Vectors " + localPath);
//                OutputStream os = sh.getOutputStream();
//                os.write(("cd /data/user/0/com.projects.balli.ffmpeg/files && ls").getBytes("ASCII"));
//                os.flush();
//                os.close();
//                sh.waitFor();
//            } catch (IOException | InterruptedException e) {
//                Log.e(TAG, "onCreate: Exception occured" );
//                Log.e(TAG, "onCreate: " + e.getMessage() );
//                e.printStackTrace();
//            }

//            try {
//                Process sh = Runtime.getRuntime().exec("su", null, null);
//                OutputStream os = sh.getOutputStream();
//                os.write(("chmod 666 /dev/video0").getBytes("ASCII"));
//                os.flush();
//                os.close();
//                sh.waitFor();
//            } catch (InterruptedException | IOException e) {
//                e.printStackTrace();
//            }
//
//            String vectorsPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//            String inputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/input_file.mp4";
//            //String inputFile = "/dev/video0";
//            String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/output_420.yuv";
//
//            //String command = "-f v4l2 -framerate 0.25 -video_size 320x240 -i " + inputFile + " -vframes 449 " + outputFile;
//            String command = "-i " + inputFile + " -s 320x240 -r 0.25 -frames:v 449 -pix_fmt yuv420p " + outputFile;
//
//            try {
//                Log.e(TAG, "onCreate: " + "process started" );
//                Process sh = Runtime.getRuntime().exec("");
//                DataOutputStream outputStream = new DataOutputStream(sh.getOutputStream());
//
//                outputStream.writeBytes("/sdcard/Vectors/SHM-12.4/bin/TAppEncoderStaticd -c /sdcard/Vectors/server/cfg/raScale.cfg -c \n");
//                outputStream.flush();
//
//                outputStream.writeBytes("exit\n");
//                outputStream.flush();
//                sh.waitFor();
//            } catch (IOException e) {
//                Log.e(TAG, "onCreate: " + "process exception" );
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            try {
//                run(ffmpeg, command);
//            } catch (FFmpegCommandAlreadyRunningException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void run(FFmpeg fFmpeg, String command) throws FFmpegCommandAlreadyRunningException {
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
                Log.e(TAG, "onProgress: " + message );
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
                makeToast("Success");

            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.e(TAG, "onFailure: " + message );
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
                        run(ffmpeg, command);
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
}
