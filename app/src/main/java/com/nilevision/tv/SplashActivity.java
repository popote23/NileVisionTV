package com.nilevision.tv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

public class SplashActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.splash);
        findViewById(R.id.logo).startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_in));
        findViewById(R.id.ring).startAnimation(AnimationUtils.loadAnimation(this, R.anim.ring_pulse));
        findViewById(R.id.title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_in));
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 3200);
    }
}
