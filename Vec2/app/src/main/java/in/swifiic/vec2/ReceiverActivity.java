package in.swifiic.vec2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import in.swifiic.vec2.helper.SharedPrefsUtils;
import in.swifiic.vec2.services.FileObserverService;

public class ReceiverActivity extends AppCompatActivity {

    TextView receiverLogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        receiverLogs = findViewById(R.id.receiver_logs_text);

        Intent intent = new Intent(this, FileObserverService.class);
        intent.putExtra(Constants.SRC_TAG, "/storage/emulated/0/Movies/Vec2/rec/");
        this.startService(intent);

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
}
