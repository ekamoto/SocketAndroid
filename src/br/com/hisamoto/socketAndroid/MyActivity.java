package br.com.hisamoto.socketAndroid;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
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

        myLocation.getLocation(getApplicationContext(), locationResult);
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

                st = new SocketTask(host, Integer.parseInt(port), 5000) {

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

            Log.i("SockeLeandro", "" + location);
            LogMap.getInstance().writeToLog("" + location.getLatitude()+" | "+location.getLongitude()+"\n");
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}