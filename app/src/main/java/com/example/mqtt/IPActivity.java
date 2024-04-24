package com.example.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IPActivity extends AppCompatActivity {

    EditText ip;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipactivity);

        ip = findViewById(R.id.ipaddress);
        btn = findViewById(R.id.connectBtn);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String value = String.valueOf(ip.getText());
                Intent i = new Intent(IPActivity.this, MainActivity.class);
                i.putExtra("ip",value);
                startActivity(i);
            }
        });
    }
}