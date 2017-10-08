package com.example.autosms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 小智
 * on 2017/10/8
 * 描述：
 */

public class SmsObserver extends ContentObserver {
    private Handler mHandler;
    private Context mContext;
    private String code;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        Log.d("DEBUG", "onChange: >>>>>>");
        Log.d("DEBUG", uri.toString());

        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        Uri inboxUrl = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUrl, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));

                Log.d("DEBUG", "发件人》》 " + address + "" + "短信内容》》》" + body);

                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);

                if (matcher.find()) {
                    code = matcher.group(0);

                    Log.d("DEBUG", "Code>>>" + code);

                    mHandler.obtainMessage(MainActivity.MSG_CODE,code).sendToTarget();

                }
            }
            c.close();
        }
    }
}
