package com.example.samplesensorproviderapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccessSensorsActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor mLight, mTemperature;

    private TextView textViewLuminosity;
    private TextView textViewTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_sensors);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textViewLuminosity = findViewById(R.id.textViewLuminosity);
        textViewTemperature = findViewById(R.id.textViewTemperature);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            LightSensorAccess lightSensorAccess = new LightSensorAccess(sensorManager, textViewLuminosity);
        } else {
            textViewLuminosity.setText("Light sensor not available on this device");
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            TemperatureSensorAccess temperatureSensorAccess = new TemperatureSensorAccess(sensorManager, textViewTemperature);
        } else {
            textViewTemperature.setText("Temperature sensor not available on this device");
        }
    }

}

