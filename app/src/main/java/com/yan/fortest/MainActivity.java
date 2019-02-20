package com.yan.fortest;

import android.animation.ValueAnimator;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final View v = findViewById(R.id.view);

    v.post(new Runnable() {
      @Override public void run() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, v.getMeasuredHeight());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            v.setTranslationY(value);
          }
        });
        valueAnimator.start();
      }
    });

    v.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        System.out.print("dfasdfasdfasdfsadfsdf");
      }
    });

    getWindow().getDecorView().postDelayed(new Runnable() {
      @Override public void run() {
        AssetManager assetManager = getAssets();
        try {
          InputStream is = assetManager.open("test.jpeg");

          Bitmap bitmap = BitmapFactory.decodeStream(is);
          getIntent().putExtra("test", bitmap);

          //startActivity(new Intent(MainActivity.this, SubActivity.class).putExtra("test", bitmap));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, 1000);
  }
}
