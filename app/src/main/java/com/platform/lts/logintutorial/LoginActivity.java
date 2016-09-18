package com.platform.lts.logintutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CCelik on 03-09-2016.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String URL_LOGIN = "http://cheetatech.me/login_tutorial.php";

    private Button btnLogin, btnLinkToRegister;
    private TextInputLayout inputEmail, inputPassword;
    private ProgressDialog pDialog;

    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (TextInputLayout) findViewById(R.id.lTextEmail);
        inputPassword = (TextInputLayout) findViewById(R.id.lTextPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = inputEmail.getEditText().getText().toString().trim();
                password = inputPassword.getEditText().getText().toString().trim();


                if (!email.isEmpty() && !password.isEmpty()) {

                    LoginUser(email, password);


                } else {

                    Toast.makeText(getApplicationContext(), "E Mail Adresi veya Şifre Hatalı!!", Toast.LENGTH_LONG).show();
                }
            }

        });


        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String nameFromPref = preferences.getString("userName", "N/A");
        String emailFromPref = preferences.getString("userEmail", "N/A");
        String passwordFromPref = preferences.getString("userPassword", "N/A");


        if(!nameFromPref.equals("N/A") && !emailFromPref.equals("N/A") && !passwordFromPref.equals("N/A")){

            LoginUser(emailFromPref, passwordFromPref);

        }
    }

    private void LoginUser(final String email, final String password) {

        String tag_string_req = "request_register";

        pDialog.setMessage("Giriş Yapılıyor ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        JSONObject user = jObj.getJSONObject("user");
                      String  user_name = user.getString("name");
                      String  user_email = user.getString("email");


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", user_name);
                        intent.putExtra("email", user_email);
                        startActivity(intent);
                        finish();


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };


        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
