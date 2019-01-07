package me.jj97181818.dangongcha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
    }
    public void goBack(View v) {
        finish();
    }
}
