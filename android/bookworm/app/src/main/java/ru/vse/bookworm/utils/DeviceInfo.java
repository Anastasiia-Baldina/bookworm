package ru.vse.bookworm.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.concurrent.atomic.AtomicReference;

public class DeviceInfo {
    private static final AtomicReference<DeviceInfo> INSTANCE = new AtomicReference<>();
    private final String deviceId;
    private final String deviceName;

    private DeviceInfo(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public static DeviceInfo instance() {
        return INSTANCE.get();
    }

    public static void createInstance(Context context) {
        var id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        var name = findDeviceName();
        var devInfo = new DeviceInfo(id, name);
        INSTANCE.compareAndSet(null, devInfo);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    private static String findDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

}
