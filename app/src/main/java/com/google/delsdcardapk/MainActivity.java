package com.google.delsdcardapk;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView logText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.checkPermission(this);
        initView();
    }

    private void initView() {
        logText = findViewById(R.id.logMsg);
    }

    public void buttonClick(View view) {

        switch (view.getId()){
            case R.id.delApk:
                ButtonUtils.delSdCardApk(this,logText);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permissions,1);
                }
            }
        }
    }
}
