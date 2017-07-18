package com.example.a15041867.c302_p09_mcafe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;

import static android.R.attr.password;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginId, etLoginPassword;
    Button btnLogin;
    private Session session;
    String apiKey,id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginId = (EditText)findViewById(R.id.editTextId);
        etLoginPassword = (EditText)findViewById(R.id.editTextPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        session = new Session(this);

        if(session.loggedin()){
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if there is network access
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                String userName = etLoginId.getText().toString();
                String password = etLoginPassword.getText().toString();
                if (networkInfo != null && networkInfo.isConnected()) {

                    //TODO 01 Insert/modify code here to send a HttpRequest to doLogin.php
                    HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/doLogin.php");
                    request.setMethod("POST");
                    request.addData("username", userName);
                    request.addData("password", password);
                    request.execute();

                    /******************************/
                    try{
                        String jsonString = request.getResponse();
                        Log.d("js","jsonString: " + jsonString);

                        JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
                        if (jsonObj.getBoolean("authenticated")){
                            // When authentication is successful
                            //TODO 02 Extract the id and API Key from the JSON object and assign them to the following variables
                            apiKey = jsonObj.getString("apikey");
                             id = jsonObj.getString("id");
                            Log.d("ui", "userId: " + id);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("loginId", id);
                            intent.putExtra("apikey", apiKey);
                            session.setLoggedin(true);
                            startActivity(intent);

                        }else{
                            TextView textView = (TextView) findViewById(R.id.textViewError);
                            textView.setText("Authentication failed, please login again");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    TextView textView = (TextView) findViewById(R.id.textViewError);
                    textView.setText("No network connection available.");
                }

            }
        });

    }
}
