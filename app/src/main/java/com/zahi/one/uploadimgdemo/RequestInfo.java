package com.zahi.one.uploadimgdemo;

/**
 * Created by zahi on 2019/1/21.
 */
public class RequestInfo<T> {

    private String version;
    private String product;
    private String token;
    private T data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
