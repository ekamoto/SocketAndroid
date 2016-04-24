package br.com.hisamoto.socketAndroid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author Leandro Shindi
 * @version 1.0 23/07/15.
 */
public class SocketTask extends AsyncTask<String, String, Boolean> {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String host;
    private int port;
    private int timeout;
    private BufferedReader in = null;
    private String serverMessage;


    /**
     * Construtor com host, porta e timeout
     *
     * @param host    host para conexão
     * @param port    porta para conexão
     * @param timeout timeout da conexão
     */
    public SocketTask(String host, int port, int timeout) {

        super();
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * Envia dados adicionais se estiver conectado
     *
     * @param data dados addicionais
     * @throws java.io.IOException
     */
    public void sendData(String data) throws IOException {

        if (socket != null && socket.isConnected()) {

            os.write(data.getBytes());
        } else {

            Log.e("SocketAndroid", "Socket fechado");
        }
    }

    public Socket getSocket() {

        return socket;
    }

    public OutputStream getOutputStream() {

        return os;
    }

    public void reconectar() {

        SocketAddress sockaddr = new InetSocketAddress(host, port);
        //socket = new Socket();
        try {
            socket.connect(sockaddr, timeout); // milisegundos
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean result = false;
        try {

            SocketAddress sockaddr = new InetSocketAddress(host, port);
            socket = new Socket();
            socket.connect(sockaddr, timeout); // milisegundos
            if (socket.isConnected()) {

                is = socket.getInputStream();
                os = socket.getOutputStream();

                for (String p : params) {

                    os.write(p.getBytes());
                }

                try {

                    //receive the message which the server sends back
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //in this while the client listens for the messages sent by the server
                    while (true) {

                        String text = "";
                        String finall = "";
                        while ((text = in.readLine()) != null) {
                            finall += text;
                            Log.i("Ekamoto","Resposta: "+finall);
                            finall = "";
                        }
                        Thread.sleep(3000);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    Log.i("Ekamoto","Erro");
                }

            } else {

                publishProgress("CONNECT ERROR");
            }

        } catch (IOException e) {

            publishProgress("ERROR");
            Log.e("SocketAndroid", "Erro de entrada e saida", e);
            result = true;
        } catch (Exception e) {

            publishProgress("ERROR");
            Log.e("SocketAndroid", "Erro generico", e);
            result = true;
        } finally {
            Log.i("Ekamoto", "Finalizou");
/*
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                Log.e("SocketAndroid", "Erro ao fechar conexao", e);
            }
            */
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }
}