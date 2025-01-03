package com.example.homework;

public interface ICallBack <T>{
    void onSuccess(T result);
    void onFailure(Exception e);
}

