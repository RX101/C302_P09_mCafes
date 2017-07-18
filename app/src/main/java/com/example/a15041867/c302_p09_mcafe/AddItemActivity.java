package com.example.a15041867.c302_p09_mcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {
    EditText etAddCategoryId, etAddDescription, etAddUnitPrice;
    Button btnAdd;
    Intent i;
    String loginId,apikey,category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        etAddDescription = (EditText)findViewById(R.id.editTextAddDescription);
        etAddUnitPrice = (EditText)findViewById(R.id.editTextAddUnitPrice);
        btnAdd = (Button)findViewById(R.id.buttonAdd);

        i = getIntent();
        loginId = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");
        category_id = i.getStringExtra("category_id");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String desc = etAddDescription.getText().toString();
                String unitPrice = etAddUnitPrice.getText().toString();


                //TODO 07 Send the HttpRequest to createNewEntry.php
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/addMenuItem.php");
                request.setMethod("POST");
                request.addData("loginId",loginId);
                request.addData("apikey",apikey);
                request.addData("category_id",category_id);
                request.addData("description",desc);
                request.addData("price",unitPrice);
                request.execute();

                try{
                    String jsonString = request.getResponse();
                    Log.d("js", "jsonString: " + jsonString);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
