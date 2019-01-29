package com.zahi.one.uploadimgdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CAMERA = 10000;

    private static final int REQUEST_CAMERA1 = 1190;
    private static final int REQUEST_CAMERA2 = 1191;
    private static final int REQUEST_ALBUM = 1192;

    private ImageView camera1Iv;
    private TextView camera1Tv;

    private ImageView camera2Iv;
    private TextView camera2Tv;

    private ImageView albumIv;
    private TextView albumTv;

    private boolean isGetCameraPermit;

    private String mCameraImgPath;
    private FileInputStream fos;

    private Map<Integer, String> mUploadImgMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 构建RxPermissions实例
        RxPermissions rxPermissions = new RxPermissions(this);

        // 申请权限
        // rxPermissions.request(Manifest.permission_group.CAMERA)

        camera1Iv = findViewById(R.id.photo_from_camera_1_iv);
        camera1Tv = findViewById(R.id.photo_from_camera_1_tv);

        camera2Iv = findViewById(R.id.photo_from_camera_2_iv);
        camera2Tv = findViewById(R.id.photo_from_camera_2_tv);

        albumIv = findViewById(R.id.photo_from_album_iv);
        albumTv = findViewById(R.id.photo_from_album_tv);

        //6.0及以上动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);//自定义的code
            } else {
                isGetCameraPermit = true;
            }
        }

        //允许公开文件uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接获取拍照的图片(获取的是缩略图)
     *
     * @param view
     */
    public void takePhoto(View view) {
        if (isGetCameraPermit) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA1);
        } else {
            Toast.makeText(this, "请允许APP使用相机后再操作", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存原图片
     *
     * @param view
     */
    public void takePhoto2(View view) {
        if (isGetCameraPermit) {
            mCameraImgPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "zahi.png";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(new File(mCameraImgPath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 把图片保存到本地
            startActivityForResult(intent, REQUEST_CAMERA2);
        } else {
            Toast.makeText(this, "请允许APP使用相机后再操作", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从相册选择
     *
     * @param view
     */
    public void fromAlbum(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
                isGetCameraPermit = true;
            } else {
                Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
                isGetCameraPermit = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA1) {
            // 直接获取图片，得到的是缩略图
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            camera1Iv.setImageBitmap(bitmap);
            camera1Tv.setText("width-->" + bitmap.getWidth() + ", height-->" + bitmap.getHeight() + ", byte-->" + bitmap.getByteCount() / 1024);

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA2) {
            // 保存原图片，获取的是原图
            try {
                fos = new FileInputStream(mCameraImgPath);
                Bitmap bitmap = BitmapFactory.decodeStream(fos);
                camera2Iv.setImageBitmap(bitmap);
                camera2Tv.setText("width-->" + bitmap.getWidth() + ", height-->" + bitmap.getHeight() + ", byte-->" + bitmap.getByteCount() / 1024);

                //保存拍照保存的图片路径到集合
                mUploadImgMap.put(0, mCameraImgPath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_CAMERA2) {
            // 删除创建的uri
            this.getContentResolver().delete(Uri.fromFile(new File(mCameraImgPath)), null, null);

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ALBUM) {
            try {
                Uri imgUri = data.getData();//系统返回照片的Uri
                if (imgUri != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // 从系统的表中查询指定Uri对应的照片
                    Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgPath = cursor.getString(columnIndex);//获取照片路径
                    cursor.close();
                    if (!TextUtils.isEmpty(imgPath)) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        albumIv.setImageBitmap(bitmap);
                        albumTv.setText("width-->" + bitmap.getWidth() + ", height-->" + bitmap.getHeight() + ", byte-->" + bitmap.getByteCount() / 1024);

                        // 保存相册获取的图片路径到集合
                        mUploadImgMap.put(1, imgPath);
                    }
                } else {
                    Toast.makeText(this, "从相册获取图片失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
