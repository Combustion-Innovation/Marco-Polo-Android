package com.ci.marcopolo;

import android.os.Environment;

/**
 * Created by Alex on 4/14/15.
 */
public class Constants {
    // callback codes

    // intent codes

    // files
    public final static String MARCOPOLO_BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MarcoPolo";
    public final static String AUTOPOLO_IMAGE_FILENAME = MARCOPOLO_BASE_DIR + "/temp_autopolo_image.jpeg";
    public final static String AUTOPOLO_VIDEO_FILENAME = MARCOPOLO_BASE_DIR + "/temp_autopolo_video.mp4";
}
