package com.yan.fortest;

import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements Runnable {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //final View v = findViewById(R.id.view);
    //v.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    System.out.print(
    //        "MainActivityMainActivityMainActivityMainActivityMainActivityMainActivity");
    //  }
    //});

    //v.postDelayed(this, 5000);

    //View vv = findViewById(R.id.view2);
    //vv.setBackground(RippleDrawableHelper.createRippleDrawable(vv, Color.parseColor("#ffffff")));
    //
    //vv.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //
    //  }
    //});
  }

  @Override public void run() {
    final View v = findViewById(R.id.view);
    float random = (float) (Math.random());
    //v.setPadding(v.getPaddingLeft() + random, v.getPaddingTop() + random,
    //    v.getPaddingRight() + random, v.getPaddingRight() + random);
    //v.setBackgroundResource(
    //    random == 0 ? R.mipmap.ic_launcher : R.mipmap.iap_vip_icon_core_purchase_close_h);
    v.setScaleX(random);
    v.setScaleY(random);
    v.postDelayed(this, 5000);
  }
}
