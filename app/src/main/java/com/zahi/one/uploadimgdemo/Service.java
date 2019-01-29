package com.zahi.one.uploadimgdemo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by zahi on 2019/1/19.
 */
public interface Service {

    /**
     * 这种方式函数必须指定@MultiPart 注解，函数参数类型为MultipartBody.Part类型，@Part 注解不需要指定字段名称。
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/upload.php")
    Call<Response> upload(@Part MultipartBody.Part file);

    /**
     * 这种方式同样要求函数必须指定@Multipart注解，函数参数使用@Part("file")注解，通过@Part注解指定字段名称(file)，
     * 但是函数参数类型不可以是MultipartBody.Part类型。
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/upload.php")
    Call<Response> upload(@Part("file") RequestBody file); // 会被当成参数上传

    /**
     * 单文件上传
     *
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST("UploadServer")
    Call<Response> upload(@Part("description") RequestBody description,
                          @Part MultipartBody.Part file);

    /**
     * 多文件上传，方式一
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("UploadServer")
    Call<Response> uploadFilesWithParts(@Part() List<MultipartBody.Part> parts);


    /**
     * 多文件上传，方式二 (属于通用方式)
     * <p>
     * 这种方式函数不可以使用@Multipart注解和@FormUrlEncoded注解，并且@Body注解只能使用一次，
     * 函数的参数类型没有要求，只要可以通过Convert转换成RequestBody就行，不过上传文件一般用直接用RequestBody。
     * 如果使用MultipartBody类型作为参数，一定要设置type为MultipartBody.FORM。
     *
     * @param body
     * @return
     */
    @POST("UploadServer")
    Call<Response> uploadFileWithRequestBody(@Body RequestBody body);

    /**
     * ###### 参数和文件混合上传 #####
     *
     * @param requestInfo
     * @param map
     * @return
     */
    @Multipart
    @POST("upload")
    Call<Response> uploadParmAndFile(@Part("requestData") RequestInfo requestInfo,
                                     @PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("upload/1")
    Call<Response> uploadParmWithFileByMultiPartBody(@PartMap Map<String, MultipartBody.Part> map);
}