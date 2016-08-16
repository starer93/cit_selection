package starer.com.cityselection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import starer.com.cityselection.Base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
           startActivity(new Intent(MainActivity.this, PrefectCityActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
