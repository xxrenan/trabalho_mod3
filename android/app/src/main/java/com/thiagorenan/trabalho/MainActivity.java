package com.thiagorenan.trabalho;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    MqttHelper mqttHelper;
    TextView text_result;
    Switch sw_atuador;

    Handler handlerLeitura;
    LeTemperatura restTemperatura;
    Runnable procAtualizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_result = findViewById(R.id.text_result);

        handlerLeitura = new Handler();
        //restTemperatura = new LeTemperatura(text_result);

        atualizaLeitura();

        startMqtt();

        sw_atuador = findViewById(R.id.sw_atuador);
        sw_atuador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                String onOff = "0";
                if (isChecked) {
                    onOff = "1";
                }
                Log.i("Switch", "Atuador "+onOff);
                mqttHelper.publish(onOff);
            }
        });
    }

    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                //dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    // Recupera leitura a cada X segundos.
    private void atualizaLeitura() {
        Log.i("atualizaLeitura ", "Chamou atualização");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.i("atualizaLeitura ", "Chamou run");
                runOnUiThread (procAtualizacao = new Runnable() { // Se não rodar na mesma thread da UI, não atualiza os objetos visíveis
                    public void run() {
                        try {
                            restTemperatura = new LeTemperatura(text_result); // Instanciar a cada interação, senão da erro diznedo que já executou
                            restTemperatura.execute();
                        } catch (Exception e) {
                            Log.i("onCreate", "erro na criação Runnable"+e.getMessage());
                        }
                    }
                });
                handlerLeitura.post(procAtualizacao);
            }
        };
        timer.schedule(task, 0, 30*1000);  // x segundos * 1000
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handlerLeitura.removeCallbacks(procAtualizacao); // Evita que o processo fique executando em background para sempre após execução do app
    }
}