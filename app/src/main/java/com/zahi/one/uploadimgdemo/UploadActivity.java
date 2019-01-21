package com.zahi.one.uploadimgdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zahi on 2019/1/19.
 */
public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void uploadSingleFile(String fileName) {
        //先创建service
        Service service = Api.getDefault();
        //构建要上传的文件
        File file = new File(fileName);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);

        String descriptionStr = "This is a description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionStr);

        Call<Response> call = service.upload(description, body);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void multiFileUpload() {
        RequestBody fb = RequestBody.create(MediaType.parse("text/plain"), "hello, retrofit");
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"),
                new File(Environment.getExternalStorageDirectory() + File.separator + "original.png"));
        Map<String, RequestBody> map = new HashMap<>();
        //这里的key必须这么写，否则服务端无法识别
        map.put("file\\; filename=\\" + "{filename}", fileRQ);

    }
}
