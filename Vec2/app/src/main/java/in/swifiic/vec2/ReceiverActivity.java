package in.swifiic.vec2;

import android.os.Bundle;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import in.swifiic.vec2.helper.SharedPrefsUtils;

public class ReceiverActivity extends AppCompatActivity {

    TextView receiverLogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        receiverLogs = findViewById(R.id.receiver_logs_text);


    }
}
