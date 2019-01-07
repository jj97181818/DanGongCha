package me.jj97181818.dangongcha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CityActivity extends AppCompatActivity {
    static final String db_name = "bus";
    static final String tb_name = "route";
    SQLiteDatabase db;  //資料庫物件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateSQLite();
                goBack();
            }
        };
        findViewById(R.id.button).setOnClickListener(onClickListener);
    }

    //操作 SQLite
    public void operateSQLite() {
        //開啟或建立資料庫
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

        //如果不存在資料表，就建立一個
        String createTable = "CREATE TABLE IF NOT EXISTS " + tb_name + "(city VARCHAR(20), route VARCHAR(40))";

        //執行 SQL 語法
        db.execSQL(createTable);
    }

    //回到主畫面
    public void goBack() {
        finish();
    }
}
