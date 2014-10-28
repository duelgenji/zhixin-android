package com.qubaopen.service;

import com.qubaopen.datasynservice.AddressService;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


/**
 * Created by duel on 14-3-26.
 */
public class GetAddressIntentService  extends IntentService {

    private AddressService addressService;


    public GetAddressIntentService() {
        super("GetAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        addressService =new AddressService(MyApplication.getAppContext());
        addressService.getAddress();
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences(
                SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SettingValues.KEY_CURRENT_ADDRESS_SAVED,true);
        editor.commit();


    }
}
