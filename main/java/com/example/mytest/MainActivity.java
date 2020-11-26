package com.example.mytest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static android.R.layout.simple_expandable_list_item_1;
import static java.lang.Math.ceil;

public class MainActivity extends AppCompatActivity {
TextView mTx;
Button btn1,btn2,btn3;
ListView lv;
int page = 1,total,select,gross,index;
String url ="https://rockchang.000webhostapp.com/mTotal.php";
public RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        select = intent.getIntExtra("select",0);
        if (select != 0){
            page = intent.getIntExtra("select",0);
        }
        mTx = findViewById(R.id.txtr);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.btn3);
        mTx.setText("第"+String.valueOf(page)+"頁");
        btn1.setOnClickListener(btn1OnClick);
        btn2.setOnClickListener(btn2OnClick);
        btn3.setOnClickListener(btn3OnClick);
        mQueue = Volley.newRequestQueue(MainActivity.this);
        mQueue.getCache().clear();
        JsonObjget();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.test:
                Intent intent = new Intent(MainActivity.this,Test.class);
                Bundle bundle = new Bundle();
                bundle.putInt("gross",gross);
                bundle.putInt("total",total);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener btn1OnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (page < total){
                page++;
                JsonObjget();
                mTx.setText("第"+String.valueOf(page)+"頁");
            }

        }
    };
    public View.OnClickListener btn2OnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (page > 1){
                JsonObjget();
                page--;
            }
            mTx.setText("第"+String.valueOf(page)+"頁");
        }
    };
    public View.OnClickListener btn3OnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final String[] volumes = new String[total];
            for (int i = 0 ; i< total ; i++){
            volumes[i] = "第"+String.valueOf(i+1)+"頁";
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("請選擇頁數");
            dialog.setItems(volumes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"你選的是:"+volumes[which],Toast.LENGTH_SHORT).show();
                    page=which+1;
                    JsonObjget();
                    mTx.setText("第"+String.valueOf(page)+"頁");
                }
            });
            dialog.show();
        }
    };
    public void JsonObjget(){
        mQueue = Volley.newRequestQueue(MainActivity.this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Rock",response.toString());
                try {
                    parserJson(response);
                } catch (JSONException e) {
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
    protected void parserJson(JSONObject jsonObject) throws JSONException {
        ArrayList<String> list = new ArrayList();
        JSONArray array = jsonObject.getJSONArray("items");
        //總數量
        gross = array.length();
        //總頁數
        if(gross%10 == 0){
            total = gross/10;
        }else{
            total = gross/10+1;
        }
        //當最後一頁沒滿時
        if (page == total && array.length()%10 !=0){
            for (int j = (page-1)*10;j<array.length();j++){
                JSONObject o = array.getJSONObject(j);
                String str ="單字："+o.getString("vocabulary")+"\n"+
                        "中文："+o.getString("chinese");
                list.add(str);
            }
        }else {
            for (int i = (page*10-9); i <= page*10; i++) {
                JSONObject o = array.getJSONObject(i-1);
                String str ="單字："+o.getString("vocabulary")+"\n"+
                        "中文："+o.getString("chinese");
                list.add(str);
            }
        }

        lv = findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String result = "索引值: " + ((page-1)*10+position);
                //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                index = (page-1)*10+position;
                Intent intent = new Intent(MainActivity.this,Detail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Index",index);
                bundle.putInt("total",gross);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
