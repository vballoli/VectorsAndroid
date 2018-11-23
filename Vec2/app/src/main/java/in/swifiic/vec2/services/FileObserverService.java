package in.swifiic.vec2.services;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

import in.swifiic.vec2.Constants;

public class FileObserverService extends Service {

    private static final String TAG = "FileObserverService";
    private FileObserver mFileObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if((intent.hasExtra(Constants.SRC_TAG))) // we store the path of directory inside the intent that starts the service
            mFileObserver = new FileObserver(intent.getStringExtra(Constants.SRC_TAG), FileObserver.CREATE) {
                @Override
                public void onEvent(int event, String path) {
                    Log.e(TAG, "onEvent: " + String.valueOf(event) + " " + path);
                }
            };
        mFileObserver.startWatching(); // The FileObserver starts watching
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
