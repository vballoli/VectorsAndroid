package in.swifiic.vec2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import in.swifiic.vec2.helper.SharedPrefsUtils;
import in.swifiic.vec2.services.FileObserverService;

public class ReceiverActivity extends AppCompatActivity {

    private static final String TAG = "ReceiverActivity";

    private File receivedFiles;

    TextView receiverLogs;
    TextView nText;
    TableLayout tableLayout;

    private FileObserverReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        receiverLogs = findViewById(R.id.receiver_logs_text);
        nText = findViewById(R.id.max_sequence_received_text);
        tableLayout = findViewById(R.id.table);


        Intent intent = new Intent(this, FileObserverService.class);
        intent.putExtra(Constants.BASE_TAG, Environment.getExternalStorageDirectory() +
                "/VectorsData");
        this.startService(intent);

        IntentFilter intentFilter = new IntentFilter(FileObserverReceiver.FILE_OBSERVER_TAG);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new FileObserverReceiver();
        registerReceiver(receiver, intentFilter);


        receivedFiles = new File(Environment.getExternalStorageDirectory() +"/Vec2/rcv");
        Log.e(TAG, "onCreate: " + Arrays.toString(receivedFiles.listFiles()));

        displayTable();
    }

    private void displayRows() {
        Set<Integer> sequencesSet = new HashSet<>();
        for (File file: receivedFiles.listFiles()) {
            sequencesSet.add(Integer.valueOf(file.toString().split("_")[1]));
        }
        List<Integer> sequencesList = new ArrayList<>(sequencesSet);
        Collections.sort(sequencesList);
        Log.e(TAG, "displayRows: "+ sequencesList);
        int factor = 5;
        for (int i = 1; i < sequencesList.get(sequencesList.size()-1); i += 0) {
            TableRow tb = new TableRow(this);
            tb.setLayoutParams(tableLayout.getLayoutParams());
            TextView nRange = new TextView(this);
            nRange.setText(String.valueOf(i)+ "-" + String.valueOf(i+factor));
            nRange.setId(i);
            TextView numberOfFiles = new TextView(this);
            numberOfFiles.setId(i);
            numberOfFiles.setText(
                    String.valueOf(countFilesInRange(receivedFiles.listFiles(), i, i+factor)));
            TextView mdCountText = new TextView(this);
            mdCountText.setId(i);
            mdCountText.setText(
                    String.valueOf(countMD(receivedFiles.listFiles(), i, i + factor)));
            TextView l0t1Text = new TextView(this);
            l0t1Text.setId(i);
            l0t1Text.setText(
                    String.valueOf(countL0T1(receivedFiles.listFiles(), i, i + factor)));
            nRange.setGravity(Gravity.CENTER);
            numberOfFiles.setGravity(Gravity.CENTER);
            mdCountText.setGravity(Gravity.CENTER);
            l0t1Text.setGravity(Gravity.CENTER);
            tb.addView(nRange);
            tb.addView(numberOfFiles);
            tb.addView(mdCountText);
            tb.addView(l0t1Text);
            tableLayout.addView(tb);
            if (i < sequencesList.get(sequencesList.size()-1)) {
                if (i + factor < sequencesList.get(sequencesList.size() - 1)) {
                    i += factor;
                } else {
                    i = sequencesList.get(sequencesList.size() - 1);
                }
                factor *= 2;
            } else {
                break;
            }
        }
    }

    private int countFilesInRange(File[] files, int start, int end) {
        int count = 0;
        for (File file: files) {
            int N = Integer.valueOf(file.toString().split("_")[1]);
            if (N >= start && N <= end) {
                count += 1;
            }
        }
        return count;
    }

    private void displayTable() {
        nText.setText(String.valueOf(lastN(receivedFiles.listFiles())));
        displayRows();
    }

    private int countMD(File[] files, int start, int end) {
        int count = 0;
        for (File file: files) {
            int N = Integer.valueOf(file.toString().split("_")[1]);
            Log.e(TAG, "countMD: " + N);
            if (file.toString().endsWith(".md") && N >= start && N <= end) {
                count += 1;
            }
        }
        return count;
    }

    private int countL0T1(File[] files, int start, int end) {
        int count = 0;
        for (File file: files) {
            int N = Integer.valueOf(file.toString().split("_")[1]);
            Log.e(TAG, "countL0T1: " + N );
            if (file.toString().contains("L0T1") && N >= start && N <= end) {
                count += 1;
            }
        }
        return count;
    }

    private int lastN(File[] files) {
        int N = 0;
        for (File file: files) {
            if (Integer.valueOf(file.toString().split("_")[1]) > N) {
                N = Integer.valueOf(file.toString().split("_")[1]);
            }
        }
        return N;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.receiver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.receiver_logout:
                SharedPrefsUtils.setStringPreference(this, Constants.APP_TYPE, Constants.CHOOSE_TYPE);
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class FileObserverReceiver extends BroadcastReceiver {

        public static final String FILE_OBSERVER_TAG = "FileObserverTag";

        @Override
        public void onReceive(Context context, Intent intent) {
            String filePath = intent.getStringExtra(Constants.FILE_CHANGE);
//            Vec2ExImLogic logic = new Vec2ExImLogic(ReceiverActivity.this);
//            logic.importFilename(filePath);
            tableLayout.removeAllViews();
            displayTable();
            Log.e(TAG, "File observer onReceive: " + filePath);
        }
    }
}
