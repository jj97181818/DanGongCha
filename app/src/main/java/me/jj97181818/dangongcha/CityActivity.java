package me.jj97181818.dangongcha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

import me.jj97181818.dangongcha.utils.CityName;
import me.jj97181818.dangongcha.api.GetRequest;
import me.jj97181818.dangongcha.api.Infos;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CityActivity extends AppCompatActivity {
    SQLiteDatabase db;  //資料庫物件
    static final String db_name = "bus";
    static final String tb_name1 = "route";
    static final String tb_name2 = "city";

    int[] chkId = {
            R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk4, R.id.chk5, R.id.chk6,
            R.id.chk7, R.id.chk8, R.id.chk9, R.id.chk10, R.id.chk11, R.id.chk12,
            R.id.chk13, R.id.chk14, R.id.chk15, R.id.chk16, R.id.chk17, R.id.chk18,
            R.id.chk19, R.id.chk20, R.id.chk21, R.id.chk22, R.id.chk23
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //簡單暴力解決 android.os.NetworkOnMainThreadException
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                goBack();
            }
        };
        findViewById(R.id.button).setOnClickListener(onClickListener);

        //開啟或建立資料庫
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

        //如果不存在路線資料表，就建立一個
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name1 + "(city VARCHAR(20), routeName VARCHAR(40))");

        //如果不存在已儲存城市資料表，就建立一個
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name2 + "(city VARCHAR(20), status int)");

        Cursor c = db.rawQuery("SELECT * FROM " + tb_name2 , null);
        //如果資料表中沒有儲存過任何資料，就增加預設資料
        if (c.getCount() == 0) {
            initCityTable();
        }

        for (int i : chkId) {
            CheckBox chk = findViewById(i);
            String city = chk.getText().toString();

            c = db.rawQuery("SELECT status FROM " + tb_name2 + " WHERE city = ?", new String[] {city});
            c.moveToFirst();

            if (c.getInt(0) == 1) {
                chk.setChecked(true);
            }
            else {
                chk.setChecked(false);
            }
        }
    }

    //操作 SQLite
    public void updateData() {
        //跑遍所有的 checkbox
        for (int i: chkId) {
            CheckBox chk = findViewById(i);
            String city = chk.getText().toString();

            //在資料表中，查詢此城市的狀態
            Cursor c = db.rawQuery("SELECT status FROM " + tb_name2 + " WHERE city = ?", new String[] {city});
            c.moveToFirst();

            //如果被按下
            if (chk.isChecked()) {

                if (c.getInt(0) == 0) {   //資料表中，此城市上次沒被勾選
                    //call API
                    for (Infos.Route route : callInfoAPI(CityName.toEnglish(city))) {
//                        Log.d("ohmy", route.routeUID);
//                        Log.d("ohmy", route.routeName);
//                        Log.d("ohmy", route.city);
//                        Log.d("ohmy", route.departureStopName);
//                        Log.d("ohmy", route.destinationStopName);

                        //新增勾選城市的基本路線
                        addCityRoute(route.city, route.routeName);
                    }

                    //將其狀態設為 1
                    updateCityStatus(city, 1);
                }
            }
            //如果沒被按下
            else {
                if (c.getInt(0) == 1) {   //資料表中，此城市上次被勾選
                    //刪除該城市的路線
                    deleteCityAllRoute(city);

                    //將其狀態設為 0
                    updateCityStatus(city, 0);
                }
            }
        }
    }

    //初始化資料表
    private void initCityTable() {
        for (String city : CityName.CHINESE) {
            addCityStatus(city, 0);
        }
    }

    //增加城市勾選資料
    private void addCityStatus(String city, int status) {
        //宣告列
        ContentValues cv = new ContentValues();
        cv.put("city", city);
        cv.put("status", status);

        //插入列
        db.insert(tb_name2, null, cv);
    }

    //編輯城市勾選資料
    private void updateCityStatus(String city, int status) {
        //宣告列
        ContentValues cv = new ContentValues();
        cv.put("status", status);

        //編輯列
        db.update(tb_name2, cv, "city = ?", new String[] {city});
    }

    //回到主畫面
    public void goBack() {
        finish();
    }

    public List<Infos.Route> callInfoAPI(String city) {
        // 建 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bus.ntut.com.tw") // 設置網路請求
                .addConverterFactory(GsonConverterFactory.create()) //設置使用 GSON 解析
                .build();

        // 建網路請求接口實例
        GetRequest request = retrofit.create(GetRequest.class);

        // 對發送請求進行封裝
        Call<Infos> call = request.getInfo(city);

        // 發送網路請求（同步）
        try {
            //成功
            return call.execute().body().routes;
        }
        catch (Exception e) {
            //失敗
            Log.d("ohmy", String.valueOf(e));
            return null;
        }
    }

    //增加已選擇的城市基本路線資料
    private void addCityRoute(String city, String routeName) {
        //宣告列
        ContentValues cv = new ContentValues();
        cv.put("city", city);
        cv.put("routeName", routeName);

        //插入列
        db.insert(tb_name1, null, cv);
    }

    //刪除已選擇的城市基本路線資料
    private void deleteCityAllRoute(String city) {
        //刪除很多列
        db.delete(tb_name1, "city = ?", new String[] {city});
    }

}
