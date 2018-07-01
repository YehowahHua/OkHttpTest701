package com.yehowah.okhttptest01_830;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yehowah.okhttptest01_830.base.BaseActivity;
import com.yehowah.okhttptest01_830.base.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//https://blog.csdn.net/fightingXia/article/details/70947701
//https://www.imooc.com/learn/764
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button HttpdoGet;
    private Button HttpdoPost;
    private Button doPostJsonBt;
    private Button doPostFileBt;
    private Button doPostUploadFileBt;
    private Button doPostDownloadFileBt;

    private TextView httpGetTv;
    private ImageView httpGetdownIv;

    private String url="http://192.168.1.103:8081/StartLogin/";
    private String jsonStr = "{\"username\":\"lisi\";\"password\":\"xxxxx\"}";//json数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpdoGet = (Button) findViewById(R.id.doGetBt);
        HttpdoPost = (Button) findViewById(R.id.doPostBt);
        doPostJsonBt = (Button) findViewById(R.id.doPostJsonBt);
        httpGetTv = (TextView) findViewById(R.id.httpGetTv);
        doPostFileBt = (Button) findViewById(R.id.doPostFileBt);
        doPostUploadFileBt = (Button) findViewById(R.id.doPostUploadFileBt);
        doPostDownloadFileBt = (Button) findViewById(R.id.doPostDownloadFileBt);
        httpGetdownIv = (ImageView) findViewById(R.id.httpGetdownIv);

        HttpdoGet.setOnClickListener(this);
        HttpdoPost.setOnClickListener(this);
        doPostJsonBt.setOnClickListener(this);
        doPostFileBt.setOnClickListener(this);
        doPostUploadFileBt.setOnClickListener(this);
        doPostDownloadFileBt.setOnClickListener(this);
        requestPermission();
    }


    private void requestPermission(){
        String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                //连接wifi权限
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                //创建热点权限
                                Manifest.permission.WRITE_SETTINGS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE };
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "权限已经都通过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                for (String deniedPerm:deniedPermission){
                    Toast.makeText(MainActivity.this,"被拒绝的权限为："+deniedPerm,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.doGetBt:
                try {
                    OkHttp_doGet();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.doPostBt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "run: post");
                            OkHttp_doPost();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.doPostJsonBt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "run: post json");
//                            OkHttp_doPost();
                            OkHttp_doPostString(url+"Login2",jsonStr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.doPostFileBt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "run: post file jpeg");
                            String jpegPath = Environment.getExternalStorageDirectory().getPath()+"/timg.jpg";
//                            OkHttp_doPost();
                            OkHttp_doPostFile(url+"postFile", jpegPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.doPostUploadFileBt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "run: post file jpeg");
                            String jpegPath = Environment.getExternalStorageDirectory().getPath()+"/timg.jpg";
//                            OkHttp_doPost();
                            OkHttp_doPostUploadFile(url+"uploadFile", jpegPath);//uploadFile
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.doPostDownloadFileBt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: post file jpeg");
                        String jpegPath = Environment.getExternalStorageDirectory().getPath()+"/timg.jpg";
//                            OkHttp_doPost();
                        OkHttp_doPostDownloadFile(url+"files/yehowah.jpg");//uploadFile
                    }
                }).start();
                break;


        }
    }

    public OkHttpClient getOkHttpClient(int mode){
        //3种创建实例方法
        //1.默认构造函数
        //2.如果要求使用现有的实例，可以通过newBuilder()方法来进行构造
        //3.通过new OkHttpClient.Builder()方法来一步一步配置一个OkHttpClient实例
        switch (mode){
            case 1:
                OkHttpClient client = new OkHttpClient();
                return client;
            case 2:
                OkHttpClient client1 = new OkHttpClient().newBuilder().build();
                return client1;
            case 3:
                OkHttpClient clientWith30sTimeout = new OkHttpClient.Builder()
                                                    .readTimeout(30, TimeUnit.SECONDS)
                                                    .build();
                return clientWith30sTimeout;
        }
        return null;
    }

    //请求体的类型为JSON格式的
    //MediaType用于描述Http请求和响应体的内容类型，也就是Content-Type。http://www.w3school.com.cn/media/media_mimeref.asp
    //提交表单
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType JPEG = MediaType.parse("image/jpeg");//jpeg,jpg


    /**
     * 从服务器中获取数据，并下载下来
     * @param url
     */
    public void OkHttp_doPostDownloadFile(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                            .get()
                            .url(url)
                            .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: code----"+response.code());
                InputStream inputStream = response.body().byteStream();

                //要进行图片压缩处理

                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetdownIv.setImageBitmap(bitmap);
                        Log.i(TAG, "onResponse: 加载图片");
                    }
                });


                //
                File file = new File(Environment.getExternalStorageDirectory(), "downYehowah.jpg");
                int len = 0;
                byte[] buf = new byte[1024];
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((len = inputStream.read(buf)) != -1){
                    fileOutputStream.write(buf,0,len);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                Log.i(TAG, "onResponse: download success");


                

            }
        });

    }

    /**
     * post 上传图片文件给服务器
     * http://localhost:8081/StartLogin/files/yehowah.jpg 浏览器打开，直接显示客户端的yehowah.jpg图片
     * @param url 注意ip地址的修改
     * @param filePath 图片文件 注意要添加读写权限
     * @throws IOException
     */
    public void OkHttp_doPostUploadFile(String url,String filePath)throws IOException{
        Log.i(TAG, "OkHttp_doPostFile: filePath--"+filePath);
        File file = new File(filePath);
        if (!file.exists()){
            Log.i(TAG, "OkHttp_doPostFile: 文件不存在");
            return;
        }
        OkHttpClient client = new OkHttpClient();

        // MultipartBuilder() 已经不可用，被MultipartBody替换
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username","yehowah")
                .addFormDataPart("password","123")
                .addFormDataPart("mPhoto","yehowah.jpg",RequestBody.create(JPEG,file))//mPhoto表单域的key
                .build();

//        RequestBody body = RequestBody.create(JPEG,file);
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "response.code(): "+response.code());
                final String res = response.body().string();
                if(response.isSuccessful()){
                    Log.i(TAG, "response.code(): "+response.code());
                    Log.i(TAG, "response.message(): "+response.message());
                    Log.i(TAG, "response.res(): "+res);

                }else new IOException("unExpected code "+response);
                //并不是UI线程，而是子线程，为了提供文件IO操作等
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetTv.setText(res);
                    }
                });
            }
        });
    }


    /**
     * post 提交图片文件给服务器
     * @param url 注意ip地址的修改
     * @param filePath 图片文件 注意要添加读写权限
     */
    public void OkHttp_doPostFile(String url,String filePath)throws IOException{
        Log.i(TAG, "OkHttp_doPostFile: filePath--"+filePath);
        File file = new File(filePath);
        if (!file.exists()){
            Log.i(TAG, "OkHttp_doPostFile: 文件不存在");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JPEG,file);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "response.code(): "+response.code());
                final String res = response.body().string();
                if(response.isSuccessful()){
                    Log.i(TAG, "response.code(): "+response.code());
                    Log.i(TAG, "response.message(): "+response.message());
                    Log.i(TAG, "response.res(): "+res);

                }else new IOException("unExpected code "+response);
                //并不是UI线程，而是子线程，为了提供文件IO操作等
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetTv.setText(res);
                    }
                });
            }
        });
    }
    /**
     * post Json数据
     * @param url
     * @param json
     * @throws IOException
     */
    public void OkHttp_doPostString(String url,String json)throws IOException{
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "response.code(): "+response.code());
                final String res = response.body().string();
                if(response.isSuccessful()){
                    Log.i(TAG, "response.code(): "+response.code());
                    Log.i(TAG, "response.message(): "+response.message());
                    Log.i(TAG, "response.res(): "+res);

                }else new IOException("unExpected code "+response);
                //并不是UI线程，而是子线程，为了提供文件IO操作等
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetTv.setText(res);
                    }
                });
            }
        });
//        Response response = client.newCall(request).execute(); 使用同步发送不过去，不知道为什么
//        Log.i(TAG, "response.code(): "+response.code());
//        Log.i(TAG, "response.res(): "+response.body().string());
//        if(response.isSuccessful()){
//            Log.i(TAG, "response.code(): "+response.code());
//            Log.i(TAG, "response.message(): "+response.message());
//            Log.i(TAG, "response.res(): "+response.body().string());
//            return response.body().string();
//        }else new IOException("unExpected code "+response);

//        return null;
    }

    /**
     * 添加表单
     */
    public void OkHttp_doPost()throws IOException{
        OkHttpClient client = new OkHttpClient();
        // FormEncodingBuilder 已经被FormBody取代
        RequestBody formBody = new FormBody.Builder()
                .add("username","yehowah")
                .add("password","ssssss")
                .build();//也可以FormBody formBody

        Request request = new Request.Builder()
                .url(url+"Login1")
                .post(formBody) //请求体
                .build();
//        Response response = client.newCall(request).execute();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "response.code(): "+response.code());
                final String res = response.body().string();
                if(response.isSuccessful()){
                    Log.i(TAG, "response.code(): "+response.code());
                    Log.i(TAG, "response.message(): "+response.message());
                    Log.i(TAG, "response.res(): "+res);

                }else new IOException("unExpected code "+response);
                //并不是UI线程，而是子线程，为了提供文件IO操作等
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetTv.setText(res);
                    }
                });
            }
        });
    }

    public void OkHttp_doGet() throws IOException {
        //1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();//全局执行者

        //2. 构建Request
        Request.Builder builder = new Request.Builder();//请求构建
        Request request = builder.get()
                                .url(url+"Login1?username=yehowah&password=xxxx")
                                .build();//builder-->>build--->>request

        //3. 将Request封装为Call
        okhttp3.Call call = okHttpClient.newCall(request);

        //4. 执行Call
        //Response response = call.execute();//一种执行方式，这里使用下面一种方式
        call.enqueue(new Callback() {//异步执行，通过回调
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
                e.printStackTrace();

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " );
                final String res = response.body().string();//注意是string()不是toString()。如果是下载文件就是response.body().bytes()。

                //如果是toString()----okhttp3.internal.http.RealResponseBody@56ad632
                //若是string()--<!DOCTYPE html>
                // <!--STATUS OK--><html>
                // <head>
                // <meta http-equiv=content-type content=text/html;charset=utf-8>
                // <meta http-equiv=X-UA-Compatible content=IE=Edge>……………………
                if(response.isSuccessful()){
                    Log.i(TAG, "response.code(): "+response.code());
                    Log.i(TAG, "response.message(): "+response.message());
                    Log.i(TAG, "response.res(): "+res);//response.body().string() 不能调用2次
                }else new IOException("unExpected code "+response);

                //并不是UI线程，而是子线程，为了提供文件IO操作等
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpGetTv.setText(res);
                    }
                });
            }
        });
    }


}
