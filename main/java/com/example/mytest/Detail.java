package com.example.mytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Detail extends AppCompatActivity {
String url;
int index,total;
String jsonResponse;
Button btn1,btn2,btn3;
RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        index = bundle.getInt("Index");
        total = bundle.getInt("total");
        mQueue = Volley.newRequestQueue(Detail.this);
        mQueue.getCache().clear();
        funJsonObjectRequest();
        btn1.setOnClickListener(btn1OnClick);
        btn2.setOnClickListener(btn2OnClick);
        btn3.setOnClickListener(btn3OnClick);
    }
    public void funJsonObjectRequest(){
        mQueue = Volley.newRequestQueue(Detail.this);
        url = "https://rockchang.000webhostapp.com/mVocabulary.php?num="+index;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Rock",response.toString());
                try{
                   String vocabulary = response.getString("Vocabulary");
                   String chinese = response.getString("Chinese");
                   String explanation = response.getString("Explanation");
                   String sentence = response.getString("Scentence");
                   String answer = response.getString("Answer");
                    jsonResponse = "";
                    jsonResponse += "單字: " + vocabulary + "\n";
                    jsonResponse += "中文: " +chinese + "\n";
                    jsonResponse += "解釋: " +"\n"+ explanation + "\n";
                    jsonResponse += "例句: " +"\n"+ sentence + "\n";
                    jsonResponse += "解答: " + answer + "\n";
                    TextView tx = findViewById(R.id.textView);
                    tx.setText(jsonResponse);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });
        mQueue.add(jsonObjReq);
    }
    public View.OnClickListener btn1OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (index > 1){
                index--;
                funJsonObjectRequest();
            }
        }
    };

    public View.OnClickListener btn2OnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (index < total-1){
                index++;
                funJsonObjectRequest();
            }
        }
    } ;
    public View.OnClickListener btn3OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Detail.this,MainActivity.class);
            intent.putExtra("select",(index/10)+1);
            startActivity(intent);
        }
    };
}
