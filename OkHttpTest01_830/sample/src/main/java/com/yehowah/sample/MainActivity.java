package com.yehowah.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doGet(View view) throws IOException {
        //1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();//全局执行者

        //2. 构建Request
        Request.Builder builder = new Request.Builder();//请求构建
        Request request = builder.get().url("www.baidu.com").build();//builder-->>build--->>request

        //3. 将Request封装为Call
        Call call = okHttpClient.newCall(request);

        //4. 执行Call
        //Response response = call.execute();//一种执行方式，这里使用下面一种方式
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: "+e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse: ");
                String res = response.body().toString();
                L.e(res);
            }
        });
    }
}






