package in.swifiic.vec2;

import android.annotation.SuppressLint;
import android.os.Environment;

public class Constants {

    /**
     * App Constants
     */
    public static final String APP_TYPE = "TYPE";
    public static final String SENDER_TYPE = "SENDER";
    public static final String RECEIVER_TYPE = "RECEIVER";
    public static final String CHOOSE_TYPE = "CHOOSE_TYPE";

    /**
     * PATH to the directory containing project files.
     */
    public static final String filesBase = "/data/data/in.swifiic.vec2/files/";

    /**
     * File names to copy to /data/data/in.swifiic.vec2/files/ directory.
     * @link Vec2
     */
    public static final String DOWN_CONVERT_STATIC_D = filesBase + "DownConvertStaticd";
    public static final String T_APP_ENCODER_STATIC_D = filesBase + "TAppEncoderStaticd";
    public static final String EXTRACT_ADD_LS = filesBase + "ExtractAddLS";

//    /**
//     * Config files directory
//     */
//    public static final String CFG = filesBase + "cfg/";

    /**
     * Config files
     */
    public static final String RASCALE = filesBase +  "raScale.cfg";
    public static final String _2L2X = filesBase + "2L-2X_vectors.cfg";
    public static final String  LAYERS2 = filesBase + "layers2.cfg";
    public static final String RESOLUTION_QUALITY = "ResolutionQuality";
    public static final String SRC_TAG = "SRC_FOLDER";

    /**
     * Video extras TAGs
     */
    public static String VIDEO_TAG = "VideoTag";
    public static final String VIDEO_RESULT = "VideoResult";

    /**
     * Resolution @link EncoderService
     */
    public static String VID_RESOLUTION = "L";

    /**
     * Receiver folder constants
     */
    public static final String RCV_TAG = "RECEIVER_FOLDER";

    /**
     * Constants from Vectors
     * https://github.com/swifiic/Vectors
     */
    public static final String BROADCAST_ACTION = "swifiic.vectors.android.BROADCAST";
    public static final String LOG_STATUS = "swifiic.vectors.android.LOG_STATUS";
    public static final String CONNECTION_STATUS = "swifiic.vectors.android.CONNECTION_STATUS";
    public static final String ANDROID_BOOT_COMPLETION = "android.intent.action.BOOT_COMPLETED";
    public static final String USER_EMAIL_ID = "swifiic.vectors.android.USER_EMAIL_ID";

    public static final String STATUS_ENABLE_BG_SERVICE = "connectionStatus";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String PAYLOAD_PREFIX = "video";
    public static final String CONNECTION_LOG_FILENAME = "ConnectionLog";
    public static final String LOGGER_FILENAME = "LogFile";
    public static final String FLDR =  "/VectorsData";
    public static final String FOLDER_LOG = "/VectorsLogs";
    public static final String ACK_PREFIX = "ack_";
    public static final String BASE_NAME =  "video_00";
    public static final String ENDPOINT_PREFIX = "Vectors_";
    public static final String CONN_UP_LOG_STR = "Connected to: ";
    public static final String ACK_FILENAME = ACK_PREFIX + BASE_NAME;

    public static final int MAX_FILES_TO_LIST = 500;
    public static final int MAX_FILES_TO_REQ = 50;

    public static final int MIN_CONNECTION_GAP_TIME = 60;
    public static final int LOG_BUFFER_SIZE = 200;
    public static final int LOG_TEXT_VIEW_LINES = 1000;
    public static final int DELAY_TIME_MS = 10;
    public static final int RESTART_NEARBY_SECS = 300;
    public static final int FILE_BUFFER_SIZE = 1024*16;
    public static final int ACK_BUFFER_SIZE = 1024*128;

}
