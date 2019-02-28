package com.yan.module2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().getDecorView().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        System.out.print("TestActivityTestActivityTestActivityTestActivityTestActivity");
      }
    });
  }
}
