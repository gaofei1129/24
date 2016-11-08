package com.example.eli.a24dian.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.eli.a24dian.R;
import com.example.eli.a24dian.utils.Constants;
import com.example.eli.a24dian.utils.Utils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.txt_weixin)
    TextView txtWei;
    @BindView(R.id.btn_share)
    Button btnShare;
    private IWXAPI api;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/Skranji_Bold.ttf");
        txtWei.setTypeface(face);
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    @Override
    protected void initData() {
        regToWx();
    }

    /**
     * 分享网页到微信
     */
    private void toWeixin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://mp.weixin.qq.com/s?__biz=MzA3NDYyNzY1NA==&mid=2650033817&idx=1&sn=afea3fef21af87fb02f071ed55c2aa99&chksm=877c01b0b00b88a65e65dd57dd922da47bcd2d73a6711684afada0364b7fdd357cfcfaa04b66&scene=0#wechat_redirect";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "益智游戏，要求四个数字运算结果等于二十四，一起来玩玩吧!";
        msg.description = "益智游戏，要求四个数字运算结果等于二十四，一起来玩玩吧!";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_main);
        msg.thumbData = Utils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = 1;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.btn_share)
    public void onClick() {
        Log.e("onClick", "------toWeixin");
        toWeixin();
    }

}
