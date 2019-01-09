package me.jj97181818.dangongcha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CityActivity extends AppCompatActivity {
    static final String db_name = "bus";
    static final String tb_name1 = "route";
    static final String tb_name2 = "city";
    ListView listview;
    ArrayList<String> routeNameList = new ArrayList<String>();

    String[] cities =  {
            "基隆市", "新北市", "台北市", "宜蘭縣", "桃園市", "新竹市", "新竹縣", "苗栗縣",
            "台中市", "彰化縣", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "台南市", "高雄市",
            "屏東縣", "台東縣", "花蓮縣", "澎湖縣", "金門縣", "連江縣", "公路客運"
    };

    int[] chkId = {
            R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk4, R.id.chk5, R.id.chk6,
            R.id.chk7, R.id.chk8, R.id.chk9, R.id.chk10, R.id.chk11, R.id.chk12,
            R.id.chk13, R.id.chk14, R.id.chk15, R.id.chk16, R.id.chk17, R.id.chk18,
            R.id.chk19, R.id.chk20, R.id.chk21, R.id.chk22, R.id.chk23
    };

    SQLiteDatabase db;  //資料庫物件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

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
        String createTable1 = "CREATE TABLE IF NOT EXISTS " + tb_name1 + "(city VARCHAR(20), routeName VARCHAR(40))";

        //如果不存在已儲存城市資料表，就建立一個
        String createTable2 = "CREATE TABLE IF NOT EXISTS " + tb_name2 + "(city VARCHAR(20), status int)";

        //執行 SQL 語法
        db.execSQL(createTable1);
        db.execSQL(createTable2);

        Cursor c = db.rawQuery("SELECT * FROM " + tb_name2 , null);
        //如果資料表中沒有儲存過任何資料，就增加預設資料
        if (c.getCount() == 0) {
            initCityTable();
        }

        for (int i : chkId) {
            CheckBox chk = findViewById(i);
            String city = chk.getText().toString();

            c = db.rawQuery("SELECT status FROM " + tb_name2 + " WHERE city = \"" + city + "\"", null);
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
            Cursor c = db.rawQuery("SELECT status FROM " + tb_name2 + " WHERE city = \"" + city + "\"", null);
            c.moveToFirst();

            //如果被按下
            if (chk.isChecked()) {

                if (c.getInt(0) == 0) {   //資料表中，此城市上次沒被勾選
                    //將其狀態設為 1
                    updateCityStatus(city, 1);

                    //中文縣市轉英文縣市
                    city = ch2en(city);

                    //call API
                    callInfoAPI(city);
                }
            }
            //如果沒被按下
            else {
                if (c.getInt(0) == 1) {   //資料表中，此城市上次被勾選
                    //將其狀態設為 0
                    updateCityStatus(city, 0);

                    //刪除該城市的路線
                    deleteCityRoute(city);
                }
            }
        }
    }

    //初始化資料表
    private void initCityTable() {
        for (String city : cities) {
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
        db.update(tb_name2, cv, "city = ?", new String[]{city});
    }

    //回到主畫面
    public void goBack() {
        finish();
    }

    //縣市名稱中英文轉換
    public String ch2en(String city) {
        switch (city) {
            case "基隆市":
                city = "Keelung";
                break;

            case "新北市":
                city = "NewTaipei";
                break;

            case "台北市":
                city = "Taipei";
                break;

            case "宜蘭縣":
                city = "YilanCounty";
                break;

            case "桃園市":
                city = "Taoyuan";
                break;

            case "新竹市":
                city = "Hsinchu";
                break;

            case "新竹縣":
                city = "HsinchuCounty";
                break;

            case "苗栗縣":
                city = "MiaoliCounty";
                break;

            case "台中市":
                city = "Taichung";
                break;

            case "彰化縣":
                city = "ChanghuaCounty";
                break;

            case "南投縣":
                city = "NantouCounty";
                break;

            case "雲林縣":
                city = "YunlinCounty";
                break;

            case "嘉義市":
                city = "Chiayi";
                break;

            case "嘉義縣":
                city = "ChiayiCounty";
                break;

            case "台南市":
                city = "Tainan";
                break;

            case "高雄市":
                city = "Kaohsiung";
                break;

            case "屏東縣":
                city = "PingtungCounty";
                break;

            case "台東縣":
                city = "TaitungCounty";
                break;

            case "花蓮縣":
                city = "HualienCounty";
                break;

            case "澎湖縣":
                city = "PenghuCounty";
                break;

            case "金門縣":
                city = "KinmenCounty";
                break;

            case "連江縣":
                city = "LienchiangCounty";
                break;

            case "公路客運":
                city = "intercity";
                break;
        }
        return city;
    }

    public void callInfoAPI(String city) {
        // 建 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://bus.ntut.com.tw") // 設置網路請求
                .addConverterFactory(GsonConverterFactory.create()) //設置使用 GSON 解析
                .build();

        // 建網路請求接口實例
        GetRequestInterface request = retrofit.create(GetRequestInterface.class);

        // 對發送請求進行封裝
        Call<Infos> call = request.getCall(city);

        // 發送網路請求（異步）
        call.enqueue(new Callback<Infos>() {
            //成功
            @Override
            public void onResponse(Call<Infos> call, Response<Infos> response) {
                for (Infos.Route route : response.body().routes) {
//                    Log.d("ohmy", route.routeUID);
//                    Log.d("ohmy", route.routeName);
//                    Log.d("ohmy", route.city);
//                    Log.d("ohmy", route.departureStopName);
//                    Log.d("ohmy", route.destinationStopName);

                    //新增勾選城市的基本路線
                    addCityRoute(route.city, route.routeName);
                }
            }

            //失敗
            @Override
            public void onFailure(Call<Infos> call, Throwable throwable) {
                Log.d("ohmy", String.valueOf(throwable));
            }
        });
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
    private void deleteCityRoute(String city) {
        //刪除很多列
        db.delete(tb_name1, "city = ?", new String[] {city});
    }

}
