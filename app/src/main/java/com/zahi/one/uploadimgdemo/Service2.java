package com.zahi.one.uploadimgdemo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by zahi on 2019/1/24.
 */
public interface Service2 {

    /**
     * 上传数量确定的多张图片
     *
     * @param description
     * @param imgs1
     * @param imgs2
     * @param imgs3
     * @param imgs4
     * @return
     */
    @POST("upload/")
    Call<ResponseBody> uploadFiles(@Part("filename") String description,
                                   @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
                                   @Part("pic\"; filename=\"image2.png") RequestBody imgs2,
                                   @Part("pic\"; filename=\"image3.png") RequestBody imgs3,
                                   @Part("pic\"; filename=\"image4.png") RequestBody imgs4);

    /**
     * 图文同时上报
     *
     * @param usermaps
     * @param avatar
     * @return
     */
    @Multipart
    @POST("upload/")
    Call<ResponseBody> register(@FieldMap Map<String, String> usermaps,
                                @Part("avatar\"; filename=\"avatar.jpg") RequestBody avatar);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFileWithParMap(@Url String url,
                                                  @FieldMap Map<String, UploadModel> maps,
                                                  @Part MultipartBody.Part file);

    @Multipart
    @POST("upload/")
    Call<ResponseBody> register(@Body RequestBody body);
}
