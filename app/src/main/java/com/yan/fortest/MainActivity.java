package com.yan.fortest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final View v = findViewById(R.id.view);

    v.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        System.out.print(
            "MainActivityMainActivityMainActivityMainActivityMainActivityMainActivity");
      }
    });
  }
}
