package com.example.eli.a24dian.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本类作为数据的工具类，负责SharedPreferences的存取，以及部分数据库中数据的拼接、转换等
 */
public class PreferencesUtils {

    //用户的轻量级数据配置，全是在这个名称的SharedPreferences中
    public static final String USER_CONFIG = "user_config";


    //是否开启智能节省流量
    public static final String USER_NUM = "succeed_num";

    /**
     * 获取是否登录过
     */
    public static int getSucceedNum(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_CONFIG, Context.MODE_PRIVATE);
        int status = sp.getInt(USER_NUM, 0);//默认是开启的
        return status;
    }

    /**
     * 设置是否登录过
     */
    public static void setSucceedNum(Context context, int enable) {
        SharedPreferences sp = context.getSharedPreferences(USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(USER_NUM, enable);
        editor.commit();
    }

}
