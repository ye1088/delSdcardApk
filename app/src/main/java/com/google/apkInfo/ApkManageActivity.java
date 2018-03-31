package com.google.apkInfo;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.google.delsdcardapk.R;

/**
 * Created by admin on 2018/3/29.
 */

public class ApkManageActivity extends ListActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.apk_manage_layout);
    }


    private class LoadAppAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
