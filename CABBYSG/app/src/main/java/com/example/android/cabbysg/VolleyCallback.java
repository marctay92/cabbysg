package com.example.android.cabbysg;

import android.os.Bundle;

public interface VolleyCallback {
    void onSuccess(String result);
    void onSuccess2(Bundle result);
    void onSuccessLogin(String userID);
    void onFailLogin();
    void onFailRegister(String remarks);
}
