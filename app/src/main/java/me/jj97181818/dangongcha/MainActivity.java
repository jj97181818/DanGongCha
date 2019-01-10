package me.jj97181818.dangongcha;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import me.jj97181818.dangongcha.utils.CityName;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;  //資料庫物件
    static final String db_name = "bus";
    static final String tb_name1 = "route";

    TextView txv;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv = findViewById(R.id.txv);

        //ListView
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ((ListView) findViewById(R.id.lv)).setAdapter(arrayAdapter);

        //開啟或建立資料庫
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

        //如果不存在路線資料表，就建立一個
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name1 + "(city VARCHAR(20), routeName VARCHAR(40))");

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] number = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9};
                int[] color = {R.id.btn_red, R.id.btn_blue, R.id.btn_green, R.id.btn_brown, R.id.btn_orange, R.id.btn_yellow, R.id.btn_trunk, R.id.btn_F};

                //蓋掉原本的標題名
                if (getTitle().toString().equals(getString(R.string.app_name))) {
                    setTitle("");
                }

                //數字按鈕
                for (int i:number) {
                    if(v.getId() == i) {
                        Button button = findViewById(v.getId());
                        setTitle(getTitle().toString() + button.getText().toString());
                    }
                }

                //顏色和幹線和 F 按鈕
                for (int i:color) {
                    if(v.getId() == i) {
                        Button button = findViewById(v.getId());
                        setTitle(button.getText().toString());
                    }
                }

                //刪除按鈕
                if(v.getId() == R.id.btn_delete) {
                    setTitle(getString(R.string.app_name));
                }

                //縣市按鈕
                if(v.getId() == R.id.btn_city) {
                    gotoCityActivity();
                    setTitle(getString(R.string.app_name));
                }

                // 每多輸入一個字
                if (getTitle().toString() != getString(R.string.app_name)) {
                    //搜尋公車路線
                    String routeName = getTitle().toString();
                    Cursor c = db.rawQuery("SELECT city, routeName FROM " + tb_name1 + " WHERE routeName LIKE ?", new String[] {"%" + routeName + "%"});

                    arrayAdapter.clear();

                    //如果有搜尋到資料
                    if (c.moveToFirst()) {
                        do {
                            arrayAdapter.add("［" + CityName.toChinese(c.getString(0)) + "］" + c.getString(1));
                        } while (c.moveToNext());
                    }
                }
            }
        };

        //listener
        ((ListView) findViewById(R.id.lv)).setOnItemClickListener(onItemClickListener);
        findViewById(R.id.btn_0).setOnClickListener(onClickListener);
        findViewById(R.id.btn_1).setOnClickListener(onClickListener);
        findViewById(R.id.btn_2).setOnClickListener(onClickListener);
        findViewById(R.id.btn_3).setOnClickListener(onClickListener);
        findViewById(R.id.btn_4).setOnClickListener(onClickListener);
        findViewById(R.id.btn_5).setOnClickListener(onClickListener);
        findViewById(R.id.btn_6).setOnClickListener(onClickListener);
        findViewById(R.id.btn_7).setOnClickListener(onClickListener);
        findViewById(R.id.btn_8).setOnClickListener(onClickListener);
        findViewById(R.id.btn_9).setOnClickListener(onClickListener);
        findViewById(R.id.btn_red).setOnClickListener(onClickListener);
        findViewById(R.id.btn_blue).setOnClickListener(onClickListener);
        findViewById(R.id.btn_green).setOnClickListener(onClickListener);
        findViewById(R.id.btn_brown).setOnClickListener(onClickListener);
        findViewById(R.id.btn_orange).setOnClickListener(onClickListener);
        findViewById(R.id.btn_yellow).setOnClickListener(onClickListener);
        findViewById(R.id.btn_F).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        findViewById(R.id.btn_city).setOnClickListener(onClickListener);
        findViewById(R.id.btn_trunk).setOnClickListener(onClickListener);
    }

    // 每次回到 MainActivity 時就會執行
    public void onResume()
    {
        super.onResume();

        Cursor c = db.rawQuery("SELECT * FROM " + tb_name1 , null);
        //如果資料表中沒有儲存過任何資料，就跳到 CityActivity 先行勾選
        if (c.getCount() == 0) {
            gotoCityActivity();
            setTitle(getString(R.string.app_name));
        }

    }

    //到 CityActivity
    public void gotoCityActivity() {
        Intent it = new Intent(this, CityActivity.class);
        startActivity(it);
    }
}
