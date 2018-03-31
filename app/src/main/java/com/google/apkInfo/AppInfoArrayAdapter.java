package com.google.apkInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.delsdcardapk.ButtonUtils;
import com.google.delsdcardapk.R;
import com.google.delsdcardapk.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 */

public class AppInfoArrayAdapter extends ArrayAdapter {

    private Context mContext = null;
    private List<ApplicationInfo> appList = null;
    private PackageManager packageManager = null;


    public AppInfoArrayAdapter(@NonNull Context context, int resource,
                               List<ApplicationInfo> appList) {

        super(context, resource);

        this.mContext = context;
        this.appList = appList;
        this.packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        if (appList == null){
            return 0;
        }
        return appList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        if (appList == null){
            return null;
        }
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        String packageName = appList.get(position).packageName;
        if (null == view){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_app_adapter,null);
        }
        ImageView iv_icon = view.findViewById(R.id.apk_icon);
        TextView tv_apkName = view.findViewById(R.id.apk_name);
        TextView tv_packageName = view.findViewById(R.id.apk_packageName);
        Button bt_uninstall = view.findViewById(R.id.uninstall_apk);

        iv_icon.setImageDrawable(appList.get(position).loadIcon(packageManager));
        tv_apkName.setText(appList.get(position).loadLabel(packageManager));
        tv_packageName.setText(packageName);
//        bt_uninstall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utils.uninstall_apk(mContext,packageName);
//            }
//        });


        return view;
    }
}
