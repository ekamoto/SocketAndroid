package br.com.hisamoto.socketAndroid;

import java.io.*;

/**
 * @author Leandro Shindi
 * @version 1.0 13/08/15.
 */
public class LogMap {

    private static LogMap log;
    private static FileWriter fw;
    private static File f;

    public LogMap() {

    }

    public static LogMap getInstance() {

        if (log == null)
            log = new LogMap();

        return log;
    }

    public void writeToLog(String texto) {

        f = new File("/sdcard/Hisamoto/log.txt");
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fw.write(texto);
        } catch (IOException e) {
            android.util.Log.e("[Log]", e.getMessage());
        }

        try {
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}