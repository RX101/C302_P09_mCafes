package com.example.a15041867.c302_p09_mcafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<category> al;
    ArrayAdapter aa;
    Intent intent;
    String loginId,apikey;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        al = new ArrayList<category>();
        lv = (ListView) findViewById(R.id.listView);

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        }
        return false;
    }

    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Open Shaared Preference
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        //create a edit Shared Preference
        SharedPreferences.Editor prefEdit = prefs.edit();
        //set a key-value pair in preferences editor
        prefEdit.putString("apikey",apikey);
        prefEdit.putString("loginId",loginId);
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        //retrieve data
        apikey=prefs.getString("apikey",apikey);
        loginId = prefs.getString("loginId",loginId);
        Toast.makeText(this,apikey +"\n" + loginId,Toast.LENGTH_LONG).show();

        // Check if there is network access
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            //TODO 03 Extract the loginId and API Key from the intent object
            intent = getIntent();
            loginId = intent.getStringExtra("loginId");
            apikey = intent.getStringExtra("apikey");

            /******************************/
            if (apikey != null) {
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/getMenuCategories.php");
                request.setMethod("POST");
                request.addData("loginId", loginId);
                request.addData("apikey", apikey);
                request.execute();

                try {
                    String jsonString = request.getResponse();
                    Log.d("js", "jsonString: " + jsonString);

                    JSONArray jsonArray = new JSONArray(jsonString);

                    // Populate the arraylist personList
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        category category1 = new category();
                        category1.setCategory_id(jObj.getString("menu_item_category_id"));
                        category1.setCategory_desc(jObj.getString("menu_item_category_description"));
                        al.add(category1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                        category category1 = (category) parent.getItemAtPosition(position);

                        intent = new Intent(getApplicationContext(), ItemActivity.class);
                        intent.putExtra("loginId", loginId);
                        intent.putExtra("apikey", apikey);
                        intent.putExtra("categoryId", category1.getCategory_id());

                        startActivity(intent);
                    }
                });
            }else {
                Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this,"No Network Connection! ",Toast.LENGTH_SHORT).show();
        }


        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

    }
}
