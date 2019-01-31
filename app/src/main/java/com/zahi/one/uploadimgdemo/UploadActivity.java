package com.zahi.one.uploadimgdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanchen.compresshelper.CompressHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by zahi on 2019/1/19.
 */
public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        String imagePath = getIntent().getStringExtra("path");

        ImageView iv1 = findViewById(R.id.iv_1);
        TextView tv1 = findViewById(R.id.tv_1);
        ImageView iv2 = findViewById(R.id.iv_2);
        TextView tv2 = findViewById(R.id.tv_2);
        try {
            Bitmap sdcardImg0 = BitmapFactory.decodeFile(imagePath);
            tv1.append("未压缩：" + "width:" + sdcardImg0.getWidth() + ",height:" + sdcardImg0.getHeight() + ",bytes:" + sdcardImg0.getByteCount() / 1024);
            iv1.setImageBitmap(sdcardImg0);
            /**南尘*/
            CompressHelper compressHelper = new CompressHelper.Builder(this)
                    .setMaxWidth(720)
                    .setMaxHeight(960)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .build();
            Bitmap bitmap1 = compressHelper.compressToBitmap(new File(imagePath));
            tv1.append("\n南尘压缩：" + "width:" + bitmap1.getWidth() + ",height:" + bitmap1.getHeight() + ",bytes:" + bitmap1.getByteCount() / 1024);

            /**compressorM*/ // 图片太大，导致bitmap oom
            /**2.1.0版本会导致OO，且自定义压缩尺寸无效*/
            /*Compressor compressor = new Compressor(this)
                    .setMaxWidth(720)
                    .setMaxHeight(960)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG);*/

            /**1.0.4版本无异常*/
            Compressor compressor = new Compressor.Builder(this)
                    .setMaxWidth(720)
                    .setMaxHeight(960)
                    .build();
            Bitmap bitmapComp = compressor.compressToBitmap(new File(imagePath));
            tv1.append("\nCompressor压缩：" + "width:" + bitmapComp.getWidth() + ",height:" + bitmapComp.getHeight() + ",bytes:" + bitmapComp.getByteCount() / 1024);
            iv2.setImageBitmap(bitmapComp);

            /**鲁班*/
            Luban.with(this)
                    .load(new File(imagePath))
                    .ignoreBy(10)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            Toast.makeText(UploadActivity.this, "开启压缩", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(File file) {
                            Log.d("OnCompressListener", "file--->" + file.getAbsolutePath());
                            Toast.makeText(UploadActivity.this, "压缩成功", Toast.LENGTH_SHORT).show();

                            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                            tv1.append("\n鲁班压缩：" + "width:" + bm.getWidth() + ",height:" + bm.getHeight() + ",bytes:" + bm.getByteCount() / 1024);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(UploadActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .launch();

            /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            Log.e("UploadActivity", "0压缩前：" + bitmap.getByteCount());

            Bitmap ratioCompressImage = ImageFactory.ratio(bitmap, 400, 400);
            iv1.setImageBitmap(ratioCompressImage);

            Log.e("UploadActivity", "0压缩后：等比例压缩：" + ratioCompressImage.getByteCount());

            Bitmap qualityCompressImage = ImageFactory.compressImage(ratioCompressImage);
            Log.e("UploadActivity", "0压缩后：质量压缩：" + qualityCompressImage.getByteCount());
            // 未压缩
            // 使用android.util.Base64
            String base64WithAndroid = FileUtils.bitmapToBase64(bitmap);
            Log.e("UploadActivity", "1压缩前base64--->" + base64WithAndroid);

            // 压缩
            // 使用 android.util.Base64
            String base64CompressWithAndroid = FileUtils.bitmapToBase64(ratioCompressImage);
            Log.e("UploadActivity", "1压缩后base64--->" + base64CompressWithAndroid);*/

            /*//加密
            String encryptBase64 = AESUtil.getInstance().encrypt(base64Compress);
            Log.e("UploadActivity", "encryptBase64--->" + encryptBase64);
            //解密
            String decryptBase64 = AESUtil.getInstance().decrypt(encryptBase64);
            Log.d("UploadActivity", "decryptBase64--->" + decryptBase64);

            Bitmap decryptBitmap = FileUtils.base64ToBitmap(decryptBase64);
            iv2.setImageBitmap(decryptBitmap);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*File file = new File("file_path");
        // 构建Body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("name", "{name}")
                .addFormDataPart("psd", "{psd}")
                .addFormDataPart("file", "{filename}", RequestBody.create(MediaType.parse("image/*"), file))
                .build();*/


        /**调用工具类压缩图片*/
        /*ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        options.uri = Uri.fromFile(new File("{imagePath}"));
        options.maxWidth = 800; //设置图片宽高
        options.maxHeight = 400;
        Bitmap bitmap = compress.compressFromUri(this, options);*/
    }

    private void uploadSingleFile(String fileName) {
        //先创建service
        Service service = Api.getDefault();
        //构建要上传的文件
        File file = new File(fileName);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
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
        map.put("file\"; filename=\"" + "{filename}", fileRQ);

    }

    /**
     * 参数及图片混合上传
     */
    private void uploadFile() {

        /**参数封装*/
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setVersion("3.0.0");
        requestInfo.setProduct("at");
        requestInfo.setToken("sud62sdfsf2ag=syd9aydss5lKl");
        requestInfo.setData(new UploadModel("zahi@test100.com"));

        /**图片封装*/
        String path1 = Environment.getExternalStorageDirectory() + File.separator + "test100.png";
        String path2 = Environment.getExternalStorageDirectory() + File.separator + "test100.jpgjpg";
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path1);
        paths.add(path2);

        Map<String, RequestBody> bodyMap = new HashMap<>();
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                File file = new File(path);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                // 参数名、文件名、文件
                bodyMap.put("file" + i + "\"; filename=\"" + file.getName(), requestBody);
            }
        }

        Api.getDefault().uploadParmAndFile(requestInfo, bodyMap).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void uploadParmWithFileByMultiPartBody() {
        File file = new File("url");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("fileParm", file.getName(), requestBody);
        Map<String, MultipartBody.Part> map = new HashMap<>();

        RequestBody requestBodyParam = RequestBody.create(MediaType.parse("application/json"), "json字符串");

        // map.put("requestData", requestBodyParam);
        map.put("file", partFile);
        Api.getDefault().uploadParmWithFileByMultiPartBody(map);
    }
}
