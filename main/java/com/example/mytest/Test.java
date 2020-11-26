package com.example.mytest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Test extends AppCompatActivity {
int totalPage,gross,min=1,max=1,testNum,page=0;
Button btn1,btn2,btn3,btnNext,btnUp,btnAswr,btnClear,btnBack;
TextView tv1,tv2,tv3,tv4,tv5,tv6;
EditText ed;
RequestQueue mQueue;
String url = "https://rockchang.000webhostapp.com/mVocabulary.php?num=";
String answer;
ArrayList<Integer> arrlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle bundle = getIntent().getExtras();
        totalPage = bundle.getInt("total");
        gross = bundle.getInt("gross");
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button4);
        btn3 = findViewById(R.id.start);
        btnBack = findViewById(R.id.btnBack);
        btnNext =findViewById(R.id.btnNext);
        btnUp = findViewById(R.id.btnUp);
        btnAswr = findViewById(R.id.btnAwer);
        btnClear = findViewById(R.id.btnClear);
        ed = findViewById(R.id.editText);
        tv1 = findViewById(R.id.textFrom);
        tv2 = findViewById(R.id.textTo);
        tv3 = findViewById(R.id.textView6);
        tv4 = findViewById(R.id.textView8);
        tv5 = findViewById(R.id.tvEx);
        tv6 = findViewById(R.id.textAnswer);
        btn1.setOnClickListener(btn1OnClick);
        btn2.setOnClickListener(btn2OnClick);
        btn3.setOnClickListener(btn3OnClick);
        btnAswr.setOnClickListener(btnAwserClick);
        btnNext.setOnClickListener(btnNextClick);
        btnUp.setOnClickListener(btnUpClick);
        btnClear.setOnClickListener(btnClearClick);
        btnBack.setOnClickListener(btnBackClick);
    }
    public OnClickListener btnBackClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Test.this,MainActivity.class);
            startActivity(intent);
        }
    };
    public  OnClickListener btnClearClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ed.setText("");
            tv6.setText("");
        }
    };
    public OnClickListener btnAwserClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int flag;
            String write;
            write = ed.getText().toString();
            flag = write.compareTo(answer);
            if (flag == 0){
                tv6.setText("~^_^~ 答對了!! " +"\n"+ "答案是："+answer);
            }else {
                tv6.setText("=_=|| 答錯了!!"+"\n"+" 答案是："+answer);
            }

        }
    };
    public OnClickListener btnUpClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (page > 0){
                page--;
                JsonObjectRequest(arrlist.get(page));
                Toast.makeText(Test.this,"這是第"+(page+1)+"頁",Toast.LENGTH_SHORT).show();
                tv6.setText("");
                ed.setText("");
            }else if (page == 0){
                Toast.makeText(Test.this,"這已經是首頁了",Toast.LENGTH_SHORT).show();
            }
        }
    };
    public OnClickListener btnNextClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (page < (testNum-1)){
                page++;
                JsonObjectRequest(arrlist.get(page));
                Toast.makeText(Test.this,"這是第"+(page+1)+"頁",Toast.LENGTH_SHORT).show();
                tv6.setText("");
                ed.setText("");
            }else {
                Toast.makeText(Test.this,"這是最後一頁了",Toast.LENGTH_SHORT).show();
            }
        }
    };
    public OnClickListener btn3OnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            arrlist = new ArrayList<Integer>();
            page = 0;
            //尾頁不得大於頭頁
            if (max < min){
                Toast.makeText(Test.this,"尾頁不得大於起始頁!",Toast.LENGTH_SHORT).show();
            //只做最後一頁不足10筆
            }else if (max == min && gross%10 !=0 && totalPage == max){
                Toast.makeText(Test.this,"不足10筆無法練習",Toast.LENGTH_SHORT).show();
            //最後一頁不足10筆，但包含前頁超過10筆
            }else if (max == totalPage && gross%10 !=0){
                for (int i = min*10-10;i<gross;i++){
                    arrlist.add(i);
                }
                ed.setText("");
                tv6.setText("");
                testNum = arrlist.size();
                Collections.shuffle(arrlist);
                JsonObjectRequest(arrlist.get(0));
                //Toast.makeText(Test.this,String.valueOf(arrlist),Toast.LENGTH_SHORT).show();
             //其他正常狀況
            }else {
                for (int i=min*10-10;i<max*10;i++){
                    arrlist.add(i);
                }
                ed.setText("");
                tv6.setText("");
                Collections.shuffle(arrlist);
                testNum = arrlist.size();
                JsonObjectRequest(arrlist.get(0));
                //Toast.makeText(Test.this,String.valueOf(arrlist.size()),Toast.LENGTH_SHORT).show();
            }
        }
    };
    public OnClickListener btn1OnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final String[] volumes = new String[totalPage];
            for (int i=0;i<totalPage;i++){
                volumes[i] = "第"+String.valueOf(i+1)+"頁";
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(Test.this);
            dialog.setTitle("請選擇頁數");
            dialog.setItems(volumes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Test.this,"你選的是:"+volumes[which],Toast.LENGTH_SHORT).show();
                    min = which+1;
                    tv1.setText("第"+String.valueOf(min)+"頁");
                }
            });
            dialog.show();
        }
    };
    public  OnClickListener btn2OnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final String[] volumes = new String[totalPage];
            for (int i=0;i<totalPage;i++){
                volumes[i] = "第"+String.valueOf(i+1)+"頁";
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(Test.this);
            dialog.setTitle("請選擇頁數");
            dialog.setItems(volumes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Test.this,"你選的是:"+volumes[which],Toast.LENGTH_SHORT).show();
                    max = which+1;
                    tv2.setText("第"+String.valueOf(max)+"頁");
                }
            });
            dialog.show();
        }
    };
    public void JsonObjectRequest(int i){
        mQueue = Volley.newRequestQueue(Test.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url+i, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Rock",response.toString());
                try{
                    String vocabulary = response.getString("Vocabulary");
                    String chinese = response.getString("Chinese");
                    String explanation = response.getString("Explanation");
                    String sentence = response.getString("Scentence");
                    answer = response.getString("Answer");
                    tv3.setText(sentence);
                    tv4.setText(chinese);
                    tv5.setText(explanation);
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
}
