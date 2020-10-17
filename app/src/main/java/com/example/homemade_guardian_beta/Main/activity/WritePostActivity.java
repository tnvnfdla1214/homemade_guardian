package com.example.homemade_guardian_beta.Main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemade_guardian_beta.R;

public class WritePostActivity extends AppCompatActivity {
    Button test_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writepost);

        test_btn = (Button)findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WritePostActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
