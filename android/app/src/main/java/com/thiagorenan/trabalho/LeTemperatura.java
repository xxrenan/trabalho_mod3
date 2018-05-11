package com.thiagorenan.trabalho;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LeTemperatura extends AsyncTask<Integer,String,String> {

    TextView text_retorno;
    URL url;
    Boolean connected;
    String retorno = "";
    String time, valor;
    Float temp;

    public LeTemperatura(TextView text_retorno){
        Log.i("LeTemperatura", "Abriu construtor");
        this.text_retorno = text_retorno;
        try {
            this.url = new URL("http://35.198.2.16:3000/temperatura");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("LeTemperatura", "Construiu!");
    }

    @Override
    protected void onPreExecute(){
        Log.i("LeTemperatura", "Conectando ao servidor...");
        text_retorno.setText("Lendo http://35.198.2.16:3000 ...");
    }
    @Override
    protected String doInBackground(Integer... params) {
        Log.i("LeTemperatura - status", "doInBackground");
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Trata erros de conexão
            urlConnection.connect();
            connected = false;
            switch(urlConnection.getResponseCode()){
                case HttpURLConnection.HTTP_OK:
                    Log.i("getResponseCode"," **OK**");
                    connected = true;
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    Log.i("getResponseCode" , " **gateway timeout**");
                    break;
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.i("getResponseCode" , "**unavailable**");
                    break;
                default:
                    Log.i("getResponseCode", " **unknown response code**.");
                    break;
            }
            if (!connected) {
                urlConnection.disconnect();
                retorno = "Não foi possível estabelecer conexão!";
                return retorno;
            }

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            JSONArray leitura = new JSONArray(builder.toString());


            for (int i = 0; i < leitura.length(); i++) {
                JSONObject c = leitura.getJSONObject(i);

                time = c.getString("time");
                if (time == null){
                    time = "Data inválida";
                }
                valor = c.getString("valor");
                retorno += time + " - Temp: " + valor.substring(0,2) + "°C\n";
            }
            urlConnection.disconnect();
        } catch (ConnectException e){
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            retorno = e.getMessage();
            e.printStackTrace();
        }
        return retorno;
    }

    @Override
    protected void onPostExecute(String retorno){
        Log.i("LeTemperatura - status", "onPostExecute");
        text_retorno.setText(retorno);
    }

}