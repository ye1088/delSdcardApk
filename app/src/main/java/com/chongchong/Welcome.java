package com.chongchong;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

/**
 * Created by admin on 2018/4/25.
 */

public class Welcome extends Activity {

    public String class_name;
    @Override
    protected void onResume() {
        super.onResume();


        startActivity(new String[]{getPackageName(),class_name});

    }

    private void startActivity(String[] paramArrayOfString){
        Intent localIntent = new Intent();
        localIntent.setComponent(new ComponentName(paramArrayOfString[0],paramArrayOfString[1]));
        localIntent.setAction(Intent.ACTION_MAIN);
        localIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(localIntent);
        finish();

    }
}
