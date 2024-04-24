package com.example.mqtt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements  MqttCallback{

    TextView t, t1,t2,t3,t4,t5,t6;
    Button b1, b2, b3;
    EditText ip;
    Switch mist, water_motor, acid_regulator, fan, led_light;
//    private static final String BROKER_URL = "tcp://192.168.0.4:1883";
    private static final String BROKER_URL = "tcp://192.168.68.75:1883";

    private static final String CLIENT_ID = "AndroidApp";
    private MqttHandler mqttHandler;

    void mqttConnect(String url){
        mqttHandler = new MqttHandler(this);
        mqttHandler.connect("tcp://" + url+":1883", CLIENT_ID);
        mqttHandler.client.setCallback(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        t = (TextView) findViewById(R.id.textbox1);
        t1 = (TextView) findViewById(R.id.textbox1);
        t2 = (TextView) findViewById(R.id.textbox2);
        t3 = (TextView) findViewById(R.id.textbox3);
        t4 = (TextView) findViewById(R.id.textbox4);
//        t5 = (TextView) findViewById(R.id.textbox5);
//        t6 = (TextView) findViewById(R.id.textbox6);

//        b1 = (Button) findViewById(R.id.button);
//        b2 = (Button) findViewById(R.id.button2);
        mist = (Switch) findViewById(R.id.mist);
        water_motor = (Switch) findViewById(R.id.water_motor);
        acid_regulator = (Switch) findViewById(R.id.acid_regulator);
        fan = (Switch) findViewById(R.id.fan);
        led_light = (Switch) findViewById(R.id.led_light);



        mist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publishMessage("hydroponics/mist","ON");
                }
                else{
                    publishMessage("hydroponics/mist","OFF");

                }
            }
        });

        water_motor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publishMessage("hydroponics/water_motor","ON");
                }
                else{
                    publishMessage("hydroponics/water_motor","OFF");

                }
            }
        });

        acid_regulator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publishMessage("hydroponics/acid_regulator","ON");
                }
                else{
                    publishMessage("hydroponics/acid_regulator","OFF");

                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publishMessage("hydroponics/fan","ON");
                }
                else{
                    publishMessage("hydroponics/fan","OFF");

                }
            }
        });

        led_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publishMessage("hydroponics/led_light","ON");
                }
                else{
                    publishMessage("hydroponics/led_light","OFF");

                }
            }
        });



//        b1.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                publishMessage("esp32/buzzer","on");
//            }
//        });
//
//        b2.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                publishMessage("esp32/buzzer","off");
//            }
//        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ip");
            mqttConnect(value);
        }



        subscribeToTopic("hydroponics/pH");
        subscribeToTopic("hydroponics/light");
        subscribeToTopic("hydroponics/temperature");
        subscribeToTopic("hydroponics/humidity");


        Log.i("Create", "onCreate: ");
    }

    @Override
    protected void onDestroy() {
        Log.d("Destroyed", "Destroyed");
        mqttHandler.disconnect();
        super.onDestroy();

    }

    private void publishMessage(String topic, String message){
        Toast.makeText(this, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic,message);
    }
    private void subscribeToTopic(String topic){
        Toast.makeText(this, "Subscribing to topic "+ topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d("Connection Lost", "connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d("m", "messageArrived: "+ topic);
        String m = message.toString();
        if(topic.equals("hydroponics/pH") ){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    t1.setText("PH: "+m);
                }
            });
        }
        if(topic.equals("hydroponics/light")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    t2.setText("Light: "+m);
                }
            });
        }
        if(topic.equals("hydroponics/temperature")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    t3.setText("Temperature: "+m);
                }
            });
        }
        if(topic.equals("hydroponics/humidity")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    t4.setText("Humidity: "+m);
                }
            });
        }


    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("msgdelivered", "msg delivered");
    }
}