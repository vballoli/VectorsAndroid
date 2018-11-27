package in.swifiic.vec2.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

import in.swifiic.vec2.Constants;
import in.swifiic.vec2.ReceiverActivity;
import in.swifiic.vec2.SenderActivity;
import in.swifiic.vec2.Vec2ExImLogic;

public class FileObserverService extends Service {

    private static final String TAG = "FileObserverService";
    private FileObserver mFileObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if((intent.hasExtra(Constants.BASE_TAG))) // we store the path of directory inside the intent that starts the service
            mFileObserver = new FileObserver(intent.getStringExtra(Constants.BASE_TAG), FileObserver.CREATE) {
                @Override
                public void onEvent(int event, String path) {
                    Log.e(TAG, "onEvent: " + String.valueOf(event) + " " + path);
                    Vec2ExImLogic logic = new Vec2ExImLogic(getApplicationContext());
                    File vectorsBase = new File(Environment.getExternalStorageDirectory() +
                            "/VectorsData");
                    for (File file: vectorsBase.listFiles()) {
                        if (!file.toString().endsWith(".json")) {
                            logic.importFilename(file.toString());
                        }
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ReceiverActivity.FileObserverReceiver.FILE_OBSERVER_TAG);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(Constants.FILE_CHANGE, path);
                    sendBroadcast(broadcastIntent);
                }
            };
        mFileObserver.startWatching(); // The FileObserver starts watching
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: Service created" );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
