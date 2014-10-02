package com.remotecontrol.lightless.remotecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.String;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

import android.widget.Toast;

import java.net.URL;


public class MyActivity extends Activity {

    private static final String TAG = "RCLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 点击锁屏按钮
    public void LockScreen(View source) {

        // String result = "";
        Log.d(TAG, "Enter LockScreen.");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    URL url = new URL("http://lightless.cn/RemoteControl/SendCode.php?sec-code=lxy&code=RC_LOCK_SCREEN");
                    HttpURLConnection httpConnect = (HttpURLConnection)url.openConnection();
                    httpConnect.connect();

                    InputStream is = httpConnect.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    result = reader.readLine();

                    httpConnect.disconnect();
                    reader.close();
                    is.close();

                    Log.d(TAG, "result: " + result);

                    if (result.trim().equals("1")) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "发送指令成功，请等待PC执行。", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "发送指令失败，可能是服务器挂了。", Toast.LENGTH_SHORT).show();
                        Looper.loop();                    }
                }
                catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "服务器挂了。。。", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
