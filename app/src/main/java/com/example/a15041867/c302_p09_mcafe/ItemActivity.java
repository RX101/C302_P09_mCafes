package com.example.a15041867.c302_p09_mcafe;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class ItemActivity extends AppCompatActivity {

    private ListView lvItem;
    private ArrayList<item> al;
    private ArrayAdapter aa;
    private Intent intent;
    private String loginId,apikey, category_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        lvItem = (ListView)findViewById(R.id.listViewItem);
        al = new ArrayList<item>();





    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_addItem) {
            Intent i = new Intent(ItemActivity.this, AddItemActivity.class);
            i.putExtra("apikey",apikey);
            i.putExtra("loginId",loginId);
            i.putExtra("category_id",category_id);
            startActivity(i);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if there is network access
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            //TODO 03 Extract the loginId and API Key from the intent object
            intent = getIntent();
            loginId = intent.getStringExtra("loginId");
            apikey = intent.getStringExtra("apikey");
            category_id = intent.getStringExtra("categoryId");

            /******************************/
            if (apikey != null) {
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/getMenuItemsByCategory.php");
                request.setMethod("POST");
                request.addData("category_id",category_id);
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
                        final item item1 = new item();
                        item1.setItem_price(jObj.getDouble("menu_item_unit_price"));
                        item1.setItem_desc(jObj.getString("menu_item_description"));
                        al.add(item1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


//                lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
//
//                        item item1 = (item) parent.getItemAtPosition(position);
//
//                        intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.putExtra("loginId", loginId);
//                        intent.putExtra("apikey", apikey);
//                        intent.putExtra("id", item1.getItem_id());
//                        startActivity(intent);
//                    }
//                });
            }
            else {
                Toast.makeText(ItemActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ItemActivity.this,"No Network Connection! ",Toast.LENGTH_SHORT).show();
        }


        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
        lvItem.setAdapter(aa);

    }
}
