package com.example.jitianbo.baidusucks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    WebView webView = null;
    Button goBtn, forwardBtn, backBtn,shezhiBtn,xiangxiBtn,shuaxinBtn,tianjiashuqianBtn,shuqianBtn,zhuyeBtn;
    EditText editText = null;
    String url = "";
    ProgressBar progressBar=null;
    RelativeLayout relativeLayout1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        webView = (WebView) findViewById(R.id.webView);
        goBtn = (Button) findViewById(R.id.go_btn);
        forwardBtn = (Button) findViewById(R.id.forward_btn);
        backBtn = (Button) findViewById(R.id.back_btn);
        shezhiBtn = (Button) findViewById(R.id.shezhi_btn);
        xiangxiBtn = (Button) findViewById(R.id.xiangxi_btn);
        shuaxinBtn = (Button) findViewById(R.id.shuaxin_btn);
        tianjiashuqianBtn = (Button) findViewById(R.id.tianjiashuqian_btn);
        shuqianBtn = (Button) findViewById(R.id.shuqian_btn);
        zhuyeBtn = (Button) findViewById(R.id.zhuye_btn);

        shezhiBtn.setVisibility(View.GONE);
        shuaxinBtn.setVisibility(View.GONE);
        tianjiashuqianBtn.setVisibility(View.GONE);


        editText = (EditText) findViewById(R.id.editText);
        editText.setText("http://");
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains("baidu.com")) {
                    view.loadUrl("https://google.ie");
                    editText.setText(view.getUrl());

                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    editText.setText(view.getUrl());
                    progressBar.setVisibility(View.INVISIBLE);
                    forwardBtn.setText(">>");
                    forwardBtn.setOnClickListener(MainActivity.this);
                } else {
                    if (progressBar.getVisibility() == View.INVISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                    forwardBtn.setText("X");
                    forwardBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            webView.stopLoading();

                        }
                    });
                }
            }

        });
        goBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        xiangxiBtn.setOnClickListener(this);
        shuaxinBtn.setOnClickListener(this);
//        tianjiashuqianBtn.setOnClickListener(this);
//        shuqianBtn.setOnClickListener(this);
        zhuyeBtn.setOnClickListener(this);

        relativeLayout1=(RelativeLayout)findViewById(R.id.relative1);
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
        else if(v.getId()==xiangxiBtn.getId()){
            if(xiangxiBtn.getText().equals("+")){
                xiangxiBtn.setText("X");
                relativeLayout1.setBackgroundColor(Color.argb(50,14,204,237));
                shezhiBtn.setVisibility(View.VISIBLE);
                shuaxinBtn.setVisibility(View.VISIBLE);
                tianjiashuqianBtn.setVisibility(View.VISIBLE);

            }
            else if(xiangxiBtn.getText().equals("X")){
                xiangxiBtn.setText("+");
                relativeLayout1.setBackgroundColor(Color.WHITE);
                relativeLayout1.getBackground().setAlpha(100);
                shezhiBtn.setVisibility(View.GONE);
                shuaxinBtn.setVisibility(View.GONE);
                tianjiashuqianBtn.setVisibility(View.GONE);
            }
        } else if(v.getId()==zhuyeBtn.getId()){
            webView.loadUrl("https://google.ie");
        }
        else if(v.getId()==shuaxinBtn.getId()){
            webView.reload();
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
        builder.setTitle("网络错误");
        builder.setMessage("是否打开网络设置");
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                dialog.dismiss();
                MainActivity.this.startActivity(intent);

            }
        });
        builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
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
        builder.setTitle("网络正常");
        builder.setMessage("这就是百度的正确用法");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
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
