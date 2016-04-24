package br.com.hisamoto.socketAndroid;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.com.hisamoto.socketAndroid.location.MyLocation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyActivity extends Activity {

    private Button btnSend;
    private TextView txtStatus;
    private TextView txtValor;
    private TextView txtHostPort;
    private static SocketTask st;
    private static MyLocation myLocation = new MyLocation();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnSend = (Button) findViewById(R.id.btnSend);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtValor = (TextView) findViewById(R.id.txtValor);
        txtHostPort = (TextView) findViewById(R.id.txtHostPort);
        btnSend.setOnClickListener(btnConnectListener);

        //myLocation.getLocation(getApplicationContext(), locationResult);
    }

    // CAPTURANDO NÚMERO DE SÉRIE DO CELULAR:

    public String getIMEI(){
        String IMEI = "";

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();

        return IMEI;
    }

// CAPTURANDO NÚMERO DE SÉRIE DO CARTÃO SIM:

    public String numCard(){
        String IMSI = "";

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMSI = telephonyManager.getSimSerialNumber();

        return IMSI;
    }

    private View.OnClickListener btnConnectListener = new View.OnClickListener() {
        public void onClick(View v) {

            String hostPort = txtHostPort.getText().toString();
            int idxHost = hostPort.indexOf(":");
            final String host = hostPort.substring(0, idxHost);
            final String port = hostPort.substring(idxHost + 1);
            Log.i("SocketAndroid", "Host: " + host + " Port: " + port);

            String data = txtValor.getText() == null ? "" : txtValor.getText().toString();

            if (!(st instanceof SocketTask)) {

                st = new SocketTask(host, Integer.parseInt(port), 3000) {

                    @Override
                    protected void onProgressUpdate(String... progress) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        txtStatus.setText(sdf.format(new Date()) + " - " + progress[0]);
                    }
                };

                // Envia os dado
                st.execute(data);
            } else {

                try {

                    st.sendData(data);
                } catch (IOException e) {
                    //reconectar();
                    Log.e("SocketAndroid", "Erro ao enviar dados");
                    e.printStackTrace();
                }
            }
        }
    };

    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {

            String text = "C;" + getIMEI() + ";" + Double.toString(location.getLatitude()) + ";" + Double.toString(location.getLongitude()) + ";" + Long.toString(location.getTime()) + ";gps";

            Log.i("SockeLeandro", "" + text);
            LogMap.getInstance().writeToLog(text + "\n");
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}