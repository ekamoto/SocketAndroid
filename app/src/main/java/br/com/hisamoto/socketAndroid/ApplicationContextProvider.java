package br.com.hisamoto.socketAndroid;

import android.app.Application;
import android.content.Context;

/**
 * @author Leandro Shindi
 * @version 1.0 27/07/15.
 */
public class ApplicationContextProvider extends Application {

    public static Context context;

    @Override
    public void onCreate() {

        context = getApplicationContext();
    }

    public static Context getContext(){

        return context;
    }
}
