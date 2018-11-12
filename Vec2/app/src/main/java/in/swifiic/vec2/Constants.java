package in.swifiic.vec2;

import android.annotation.SuppressLint;

public class Constants {

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

    /**
     * Video extras TAGs
     */
    public static String VIDEO_TAG = "VideoTag";
    public static final String VIDEO_RESULT = "VideoResult";

    /**
     * Resolution @link EncoderService
     */
    public static String VID_RESOLUTION = "L";
}
