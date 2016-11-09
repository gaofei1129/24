package com.example.eli.a24dian.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eli.a24dian.R;
import com.example.eli.a24dian.adapter.CalculateAdapter;
import com.example.eli.a24dian.model.Card;
import com.example.eli.a24dian.model.ExpressionParser;
import com.example.eli.a24dian.utils.Constants;
import com.example.eli.a24dian.utils.CustomToast;
import com.example.eli.a24dian.utils.PreferencesUtils;
import com.example.eli.a24dian.utils.Utils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    int x = 0, y = 0, m = 0, n = 0;
    @BindView(R.id.grid_calculate)
    GridView gridCalculate;
    CalculateAdapter adapter;
    List<String> list = new ArrayList<String>();
    @BindView(R.id.txt_input)
    TextView txtInput;
    @BindView(R.id.txt_num1)
    TextView txtNum1;
    @BindView(R.id.txt_num2)
    TextView txtNum2;
    @BindView(R.id.txt_num3)
    TextView txtNum3;
    @BindView(R.id.txt_num4)
    TextView txtNum4;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.txt_new)
    TextView txtNew;
    @BindView(R.id.txt_help)
    TextView txtHelp;
    @BindView(R.id.txt_about)
    TextView txtAbout;

    private PopupWindow popupSucessWindow;
    private PopupWindow popupFaildWindow;
    String[] strNumArray = null;
    private IWXAPI api;
    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final int THUMB_SIZE = 150;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        regToWx();

        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/BubbleBaZ.ttf");
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/Skranji_Bold.ttf");
        txtInput.setTypeface(face);
        txtNum1.setTypeface(face);
        txtNum2.setTypeface(face);
        txtNum3.setTypeface(face);
        txtNum4.setTypeface(face);

        txtInfo.setTypeface(face);
        txtNew.setTypeface(face);
        txtHelp.setTypeface(face);
        txtAbout.setTypeface(face);
        //txtInput.setTypeface(face);
        //tf = new TfUtils();
        replace();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 4; i++) {
            list.add(i + "");
        }
        list.add("(");
        list.add(")");
        list.add("+");
        list.add("-");
        list.add("*");
        list.add("/");
        list.add("回退");
        list.add("确定");
        adapter = new CalculateAdapter(this, list);
        gridCalculate.setAdapter(adapter);
        gridCalculate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        txtInput.setText(txtInput.getText().toString() + x + "");
                        break;
                    case 1:
                        txtInput.setText(txtInput.getText().toString() + y + "");
                        break;
                    case 2:
                        txtInput.setText(txtInput.getText().toString() + m + "");
                        break;
                    case 3:
                        txtInput.setText(txtInput.getText().toString() + n + "");
                        break;
                    case 4:
                        txtInput.setText(txtInput.getText().toString() + "(");
                        break;
                    case 5:
                        txtInput.setText(txtInput.getText().toString() + ")");
                        break;
                    case 6:
                        txtInput.setText(txtInput.getText().toString() + "+");
                        break;
                    case 7:
                        txtInput.setText(txtInput.getText().toString() + "-");
                        break;
                    case 8:
                        txtInput.setText(txtInput.getText().toString() + "*");
                        break;
                    case 9:
                        txtInput.setText(txtInput.getText().toString() + "/");
                        break;
                    case 10:
                        if (txtInput.getText().toString().length() > 0) {
                            txtInput.setText(txtInput.getText().toString().substring(0, txtInput.getText().toString().length() - 1));
                        } else {
                            txtInput.setText("");
                        }
                        break;
                    case 11:
                        toCalculate(txtInput.getText().toString().trim(), view);
                        break;
                }
            }
        });
    }

    String expressionPro = "";

    private boolean toCalculate(String expression, View view) {
        if (!stringArrayCompare(strNumArray, checkNum(expression))) {
            showFaildPopupWindow(view, "Input is wrong.");
            return false;
        }
        ExpressionParser ep = new ExpressionParser(expression);
        int result = 0;
        try {
            result = (int) ep.parse();
            System.out.print("表达式 " + expression + " 计算结果为：" + ep.parse());
        } catch (Exception e1) {
            System.out.println("表达式解析时产生错误:" + e1.getMessage());
            e1.printStackTrace();
        }
        //int result = (int)Calculator.conversion(expression);
        Log.e("toCalculate", "-----" + result);
        if (result == 24) {
            //CustomToast.showToast(this, "正确: " + expression + " = " + result);
            if (!expressionPro.equals(expression)) {
                PreferencesUtils.setSucceedNum(this, PreferencesUtils.getSucceedNum(this) + 1);
            }
            expressionPro = expression;
            showSucceedPopupWindow(view, expression + " = " + (int) result);

            return true;
        } else if (result == 0) {
            showFaildPopupWindow(view, "Input is wrong.");
            return false;
        } else {
            showFaildPopupWindow(view, expression + " != 24");
            System.out.println(expression + " = " + result);
            return false;
        }
    }

    /**
     * 判断是否用到并且只用到随机到的四个数字
     */
    private String[] checkNum(String str) {
        String strNum = "";
        String[] strNumArray = null;
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                strNum += m.group() + ",";
            System.out.println(m.group());

        }
        if (strNum.length() > 0) {
            strNum.substring(0, strNum.length() - 1);
            strNumArray = strNum.split(",");
            Arrays.sort(strNumArray);
            return strNumArray;
        } else {
            return null;
        }

    }

    private void replace() {
        x = (int) ((Math.random()) * 13 + 1);
        y = (int) ((Math.random()) * 13 + 1);
        m = (int) ((Math.random()) * 13 + 1);
        n = (int) ((Math.random()) * 13 + 1);

        list.remove(0);
        list.add(0, x + "");
        list.remove(1);
        list.add(1, y + "");
        list.remove(2);
        list.add(2, m + "");
        list.remove(3);
        list.add(3, n + "");
        adapter.notifyDataSetChanged();

        txtNum1.setText(x + "");
        txtNum2.setText(y + "");
        txtNum3.setText(m + "");
        txtNum4.setText(n + "");
        String str = x + "," + y + "," + m + "," + n;
        strNumArray = str.split(",");
        Arrays.sort(strNumArray);

        txtInput.setText("");
    }

    /**
     * 分享网页到微信
     */
    private void toWeixin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://fir.im/yec8";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我在益智游戏24里面答对了 " + PreferencesUtils.getSucceedNum(this) + " 道题，一起来玩玩吧!";
        msg.description = "我在益智游戏24里面答对了 " + PreferencesUtils.getSucceedNum(this) + " 道题，一起来玩玩吧!";
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

    public static boolean stringArrayCompare(String[] b, String[] c) {
        if (c == null || b.length != c.length) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < c.length; i++) {
            if (!b[i].equals(c[i])) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    private void showSucceedPopupWindow(View view, String str) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.layout_poput_sucess, null);
        Button btnShare = (Button) contentView.findViewById(R.id.btn_toshare);
        TextView txtResult = (TextView) contentView.findViewById(R.id.txt_result);
        TextView txtSucceedNum = (TextView) contentView.findViewById(R.id.txt_succeed_num);
        TextView txtSucceed = (TextView) contentView.findViewById(R.id.txt_succee);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/CarterOne.ttf");
        txtResult.setTypeface(face);
        txtSucceedNum.setTypeface(face);
        txtSucceed.setTypeface(face);
        btnShare.setTypeface(face);

        txtResult.setText(str);
        txtSucceedNum.setText(PreferencesUtils.getSucceedNum(this) + "");
        ImageView imgNext = (ImageView) contentView.findViewById(R.id.img_next);
        ImageView imgClose = (ImageView) contentView.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSucessWindow.dismiss();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSucessWindow.dismiss();
                replace();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getBitmap(v);
                toWeixin();
                replace();
                popupSucessWindow.dismiss();
            }
        });
        popupSucessWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);

        popupSucessWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupSucessWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        //popupWindow.showAsDropDown(view);
        popupSucessWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showFaildPopupWindow(View view, String str) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.layout_poput_filed, null);

        TextView txtResult = (TextView) contentView.findViewById(R.id.txt_result);
        TextView txtFiled = (TextView) contentView.findViewById(R.id.txt_filed);
        Button btnAgain = (Button) contentView.findViewById(R.id.btn_again);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/CarterOne.ttf");
        txtResult.setTypeface(face);
        txtFiled.setTypeface(face);
        btnAgain.setTypeface(face);
        txtResult.setText(str);

        ImageView imgNext = (ImageView) contentView.findViewById(R.id.img_next);
        ImageView imgClose = (ImageView) contentView.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFaildWindow.dismiss();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFaildWindow.dismiss();
                replace();
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFaildWindow.dismiss();
                txtInput.setText("");
            }
        });
        popupFaildWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);

        popupFaildWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupFaildWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        //popupWindow.showAsDropDown(view);
        popupFaildWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 去评分
     */
    private void toGrade() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void toAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);
    }

    @OnClick({R.id.txt_info, R.id.txt_new, R.id.txt_help, R.id.txt_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_info:
                Card.traverse(new int[]{x, y, m, n});
                Set<String> stringSet = Card.traverse(new int[]{x, y, m, n});
                if (stringSet != null) {
                    for (String s : stringSet) {
                        Toast.makeText(this, s + " = 24！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    CustomToast.showToast(this, "无解,请刷新!");
                }
                break;
            case R.id.txt_new:
                replace();
                break;
            case R.id.txt_help:
                toGrade();
                break;
            case R.id.txt_about:
                toAbout();
                break;
        }
    }
}
