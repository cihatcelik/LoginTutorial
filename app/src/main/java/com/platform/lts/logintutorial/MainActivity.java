package com.platform.lts.logintutorial;

import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOut = (ImageView) findViewById(R.id.logoutImage);
        logOut.setOnClickListener(logOut_Listener);
    }

    View.OnClickListener logOut_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            clearSharedPreferences();
            finish();
        }
    };

    private void clearSharedPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("userName");
        editor.remove("userPassword");
        editor.remove("userEmail");

        editor.commit();
    }
}
