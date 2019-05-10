package com.saber.green.decisionshake.ui.activity;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saber.green.decisionshake.R;
import com.saber.green.decisionshake.utils.ShakeUtils;
import com.squareup.seismic.ShakeDetector;

import androidx.appcompat.app.AppCompatActivity;

public class ShakeActivity extends AppCompatActivity {

    TextView shakeText;
    Button nextButton;
    ShakeUtils shakeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        shakeText = findViewById(R.id.shake_text_view);
        nextButton = findViewById(R.id.next);
        initShakeUtils();
        onNextButtonClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        shakeUtils.startHearShake();
    }

    @Override
    public void onPause() {
        super.onPause();
        shakeUtils.stopHearShake();
    }


    //TODO remove for final build
    public void onNextButtonClick() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResultActivity();
            }
        });
    }

    public void initShakeUtils() {
        shakeUtils = new ShakeUtils(this, new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                shakeUtils.stopHearShake();
                startResultActivity();
            }
        });
    }

    public void startResultActivity() {
        Intent intent = new Intent(ShakeActivity.this, ResultActivity.class);
        startActivity(intent);
    }

}
