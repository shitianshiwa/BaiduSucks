package com.example.jitianbo.baidusucks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    WebView webView = null;
    Button goBtn, forwardBtn, backBtn, stopBtn, refreshBtn,clearBtn;
    EditText editText = null;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        goBtn = (Button) findViewById(R.id.go_btn);
        forwardBtn = (Button) findViewById(R.id.forward_btn);
        backBtn = (Button) findViewById(R.id.back_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);
        refreshBtn = (Button) findViewById(R.id.refresh_btn);
        clearBtn = (Button)findViewById(R.id.clear_btn);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText("http://");
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
            @Override
            public void onLoadResource(WebView view, String url){
                if(url.contains("baidu.com")){
//                    ||view.getOriginalUrl().contains("www.baidu.com")||view.getUrl().contains("www.baidu.com")
                    view.loadUrl("https://google.ie");
                    editText.setText(view.getUrl());
                }
            }
        });
        goBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == goBtn.getId()) {
            url = editText.getText().toString();
            if (url.contains("www.baidu.com")) {//&&!url.equals("http://www.baidu.com")
                Handler mHandler = new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ping()) {
                            Toast.makeText(MainActivity.this, "true", Toast.LENGTH_SHORT).show();
                            dialog2();

                        } else {
                            Toast.makeText(MainActivity.this, "false", Toast.LENGTH_SHORT).show();
                            dialog1();
                        }
                    }
                });
            } else {
                webView.loadUrl(url);
                editText.setText(webView.getUrl());
            }

        }
        else if (v.getId() == forwardBtn.getId()) {
            webView.goForward();
            editText.setText(webView.getUrl());
        }
        else if (v.getId() == backBtn.getId()) {
            webView.goBack();
            editText.setText(webView.getUrl());
        }
        else if (v.getId() == stopBtn.getId()) {
            webView.stopLoading();
            editText.setText(webView.getUrl());
        }
        else if (v.getId() == refreshBtn.getId()) {
            webView.reload();
            editText.setText(webView.getUrl());
        }
        else if(v.getId()==clearBtn.getId()){
            editText.setText("http://");
        }
    }


    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
//            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
//            Log.d("----result---", "result = " + result);
        }
        return false;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();
                return true;
            }
            else
            {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection Error");
        builder.setMessage("Do you want to open network setting?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                dialog.dismiss();
                MainActivity.this.startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void dialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection Succeed");
        builder.setMessage("Your Internet works well");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
