package in.swifiic.vec2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import in.swifiic.vec2.helper.SharedPrefsUtils;

import static in.swifiic.vec2.Constants.APP_TYPE;
import static in.swifiic.vec2.Constants.CHOOSE_TYPE;
import static in.swifiic.vec2.Constants.RECEIVER_TYPE;
import static in.swifiic.vec2.Constants.SENDER_TYPE;

public class SplashActivity extends AppCompatActivity {

    Button senderButton;
    Button receiverButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        switch (SharedPrefsUtils.getStringPreference(this, APP_TYPE, CHOOSE_TYPE)) {
            case SENDER_TYPE:
                startActivity(new Intent(this, SenderActivity.class));
                finish();
                break;
            case RECEIVER_TYPE:
                startActivity(new Intent(this, ReceiverActivity.class));
                finish();
                break;
            default:
                setup();
                break;
        }
    }

    private void setup() {
        senderButton = findViewById(R.id.splash_sender_button);
        receiverButton = findViewById(R.id.splash_receiver_button);

        senderButton.setOnClickListener((v) -> {
            SharedPrefsUtils.setStringPreference(this, APP_TYPE, SENDER_TYPE);
            Intent intent = new Intent(SplashActivity.this, SenderActivity.class);
            startActivity(intent);
            finish();
        });

        receiverButton.setOnClickListener((v) -> {
            SharedPrefsUtils.setStringPreference(this, APP_TYPE, RECEIVER_TYPE);
            Intent intent = new Intent(SplashActivity.this, ReceiverActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
