package in.swifiic.vec2.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import in.swifiic.vec2.Constants;
import in.swifiic.vec2.MainActivity;
import in.swifiic.vec2.helper.FfmpegHelper;

public class EncoderService extends IntentService {

    private final String TAG = this.getClass().getName();

    String resolution;
    String dc_res_1;
    String dc_res_2;
    String srcWidthStr;
    String framerate;
    String numframes;
    String framerateStr;

    public EncoderService() {
        super("EncoderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "onHandleIntent: Intentervice has triggered" );
        String videoFileName = null;
        if (intent != null) {
            videoFileName = intent.getStringExtra(Constants.VIDEO_TAG);
            videoFileName = videoFileName.replace(".mp4", "");
            if (Constants.VID_RESOLUTION.equals("L")) {
                //Low resolution parameters
                resolution = "320x240";
                dc_res_1 = "320 240";
                dc_res_2 = "160 120";
                framerate = "0.25";
                numframes = "5";
                srcWidthStr="-wdt0 160 -hgt0 120 -wdt1 320 -hgt1 240";
            } else {
                //Medium resolution parameters
                resolution = "640x480";
                dc_res_1 = "640 480";
                dc_res_2 = "320 240";
                framerate = "0.25";
                numframes = "5";
                srcWidthStr = "-wdt0 320 -hgt0 240 -wdt1 640 -hgt1 480";
            }
            framerateStr="-fr0 1 -fr1 1";
            executeEncoding(videoFileName);
        } else {
            Log.e(TAG, "onHandleIntent: Null intent");
        }
    }
    
    private void executeEncoding(String filename) {
        while(!getRawFrames(filename));
        while(!downConvertFile(filename));
        while(!encodeFile(filename));
    }

    /**
     * Extracts raw frames and creates two files: filename.yuv and filename_yuv420p.yuv
     *
     * @param filename Absolute path of the input file without file extension.
     * @return boolean true: Process has completed successfully.
     */
    private boolean getRawFrames(String filename) {
        Log.e(TAG, "getRawFrames: Starting frames" );
        String inputFileName = filename + ".mp4";
        String intermediateFileName = inputFileName.replace(".mp4", ".yuv");
        String command_yuv =  "-i " + inputFileName
                + " -s " + resolution + " -r " + framerate + " " + intermediateFileName;

        while(!FfmpegHelper.run(this, command_yuv, "YUV command 1"));
        String outputFileName = intermediateFileName.replace(".yuv", "_yuv420p.yuv");
        String command_yuv_q = "-y -r " + framerate + " -s " + resolution + " -pix_fmt yuyv422 -i " + intermediateFileName
                + " -pix_fmt yuv420p -r " + framerate + " -s " + resolution + " " + outputFileName;

        while(!FfmpegHelper.run(this, command_yuv_q, "YUV command 2"));
        return true;

    }

    /**
     * Downsizes the file creating filename_Q.yuv
     * @param filename Absolute path of the file without the file extension.
     * @return boolean - true: process has completed successfully.
     */
    private boolean downConvertFile(String filename) {
        Log.e(TAG, "downConvertFile: Down converting " );
        String inputFileName = filename + "_yuv420p.yuv";
        String outputFileName = inputFileName.replace("_yuv420p.yuv", "_Q.yuv");
        String downConvertArguments = dc_res_1 + " " + inputFileName + " " + dc_res_2 + " " +
                outputFileName;
        String downConvertCommand = Constants.DOWN_CONVERT_STATIC_D + " " + downConvertArguments;
        Log.e(TAG, "downConvertFile: " + downConvertCommand );
        CommandResult downConvertResult = Shell.SH.run(downConvertCommand);
        if (downConvertResult.isSuccessful()) {
            Log.e(TAG, "downConvertFile: " + filename + " Successful");
            return true;
        } else {
            Log.e(TAG, "downConvertFile: " + filename + " Failed\n" +
                    downConvertResult.getStderr());
            return false;
        }
    }

    /**
     * Encodes the file using the previously created downsized file_Q.yuv and its original raw file.yuv
     * and outputs file.bin
     * @param filename Absolute path of the file without the file extension.
     * @return boolean - true: process has completed successfully.
     */
    private boolean encodeFile(String filename) {
        Log.e(TAG, "encodeFile: Encoding ");
        String input_0 = filename + "_Q.yuv";
        String input_1 = filename + ".yuv";
        String output_bin = filename + ".bin";
        String inputOutputFiles = "-i0 " + input_0 + " -i1 " + input_1 + " -b " + output_bin;
        String encoderArguments = " -c " + Constants.RASCALE + " -c " + Constants._2L2X + " -c " +
                Constants.LAYERS2 + " " + inputOutputFiles + " "  + srcWidthStr + " " + framerateStr +
                " -f " + numframes;
        String encoderCommand = Constants.T_APP_ENCODER_STATIC_D + encoderArguments;
        CommandResult encoderResult = Shell.SH.run(encoderCommand);
        if (encoderResult.isSuccessful()) {
            Log.e(TAG, "encodeFile: " + filename + " Successful");
            extractFile(filename);
            return true;
        } else {
            Log.e(TAG, "encodeFile: " + filename + " Failed\n" +
                    encoderResult.getStderr());
            return false;
        }
    }

    /**
     * Extracts 5 spatial and 2 temporal layers and creates a file_video.bin file
     * from the file.bin created while Encoding and exits from the service.
     * @param filename Absolute path of the input file without it's extension.
     */
    private void extractFile(String filename) {
        Log.e(TAG, "extractFile: Extracting ");
        String file_bin = filename + ".bin";
        String videoName = filename +  "_video.bin";
        String extractCommand = Constants.EXTRACT_ADD_LS + " " + file_bin + " " + videoName + " 5 2";
        CommandResult extractResult = Shell.SH.run(extractCommand);
        if (extractResult.isSuccessful()) {
            Log.e(TAG, "extractFile: " + filename + " Successful" );
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.EncoderServiceReceiver.EncoderServiceReceiverTAG);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(Constants.VIDEO_RESULT, filename);
            sendBroadcast(broadcastIntent);
        } else {
            Log.e(TAG, "extractFile: " + filename + " Failed\n" + extractResult.getStderr());
        }
    }
}
