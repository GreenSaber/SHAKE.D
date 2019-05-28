package com.saber.green.std.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.saber.green.std.R;
import com.saber.green.std.utils.ShakeUtils;
import com.squareup.seismic.ShakeDetector;

import androidx.appcompat.app.AppCompatActivity;

public class ShakeActivity extends AppCompatActivity {

    private TextView shakeText;
    private ImageView phoneView;
    private ImageView shakeView;
    ShakeUtils shakeUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        shakeText = findViewById(R.id.shake_text_view);
        phoneView = findViewById(R.id.phoneView);
        shakeView = findViewById(R.id.shakeView);

        initShakeUtils();
        onPhoneImageClick();
        onShakeImageClick();
        animatePhoneShake();
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

    public void onPhoneImageClick() {
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResultActivity();
            }
        });
    }

    public void onShakeImageClick() {
        shakeView.setOnClickListener(new View.OnClickListener() {
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
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    public void animatePhoneShake() {
        Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        phoneView.setAnimation(shakeAnimation);
    }
}
