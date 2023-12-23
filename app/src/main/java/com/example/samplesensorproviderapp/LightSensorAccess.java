package com.example.samplesensorproviderapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LightSensorAccess implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mLight;
    private TextView sensorField;

    // Adicione variáveis relacionadas ao MQTT
    private Mqtt5BlockingClient mqttClient;
    private String mqttBrokerURI = "3.225.116.68"; // Substitua pelo URI do seu broker

    public LightSensorAccess(SensorManager sm, TextView tv) {
        sensorManager = sm;
        sensorField = tv;
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Configurar o cliente MQTT (ajuste conforme necessário)
        mqttClient = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(mqttBrokerURI)
                .buildBlocking();

        // Registrar ouvinte do sensor de luminosidade
        if (mLight != null) {
            sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Trate o caso em que o sensor de luminosidade não está disponível no dispositivo.
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorField.setText(String.valueOf(lux));

        publishToMQTT("light_sensor", String.valueOf(lux));
    }

    private void publishToMQTT(String topic, String message) {
        try {
            mqttClient.connect();
            mqttClient.publishWith()
                    .topic(topic)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .payload(message.getBytes(StandardCharsets.UTF_8))
                    .send();
            mqttClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
    }
}
