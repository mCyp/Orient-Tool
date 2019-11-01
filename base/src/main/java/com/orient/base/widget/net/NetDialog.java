package com.orient.base.widget.net;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.orient.base.R;

import java.util.regex.Pattern;


/**
 * 设置网段的Dialog
 *
 * @Auther WangJie on 2018/2/2.
 */

public class NetDialog extends Dialog {

    // 确定按钮
    private Button mSure;
    // 取消按钮
    private Button mNo;
    // IP
    private EditText mIp;
    // 端口号
    private EditText mPort;
    private Toolbar mToolbar;

    private Context mContext;
    private AddressCallback callback;

    private String ipAddress;
    private String themeColor;

    public NetDialog(@NonNull Context context, @StyleRes int themeResId, AddressCallback callback) {
        super(context, themeResId);
        this.mContext = context;
        this.callback = callback;
    }

    public NetDialog(@NonNull Context context, @StyleRes int themeResId) {
        this(context, themeResId, null);
    }

    /**
     * 设置回调
     *
     * @param callback AddressCallback
     */
    public void setAddressCallback(AddressCallback callback) {
        this.callback = callback;
    }

    /**
     * 设置主题色
     *
     * @param color 颜色代码
     */
    public void setThemeColor(String color) {
        themeColor = color;
    }

    /**
     * 设置ip地址
     *
     * @param ipAddress ip地址
     */
    public void setInfo(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.net_dialog);
        initWidget();
    }

    // 初始化控件
    private void initWidget() {
        mSure = findViewById(R.id.btn_yes);
        mNo = findViewById(R.id.btn_no);
        mIp = findViewById(R.id.edit_ip);
        mPort = findViewById(R.id.edit_port);
        mToolbar = findViewById(R.id.toolbar);

        if (!TextUtils.isEmpty(themeColor))
            initColor();

        String ipAddress = this.ipAddress;
        if (!TextUtils.isEmpty(ipAddress)) {
            int startPos = ipAddress.indexOf(":") + 3;
            int lastPos = ipAddress.lastIndexOf(":") + 5;
            ipAddress = ipAddress.substring(startPos, lastPos);
            String[] ips = ipAddress.split("\\:");
            mIp.setText(ips[0]);
            mPort.setText(ips[1]);
        }

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIp.setText("");
                mPort.setText("");
            }
        });

        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mIp.getText().toString();
                String port = mPort.getText().toString();
                if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
                    Toast.makeText(mContext, "IP或者端口号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ipPattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
                String portPattern = "([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])";
                boolean isIpMatch = Pattern.matches(ipPattern, ip);
                boolean isPortMatch = Pattern.matches(portPattern, port);
                if (!isPortMatch || !isIpMatch) {
                    Toast.makeText(mContext, "IP或者端口号格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (callback != null)
                    callback.onAddAddress(ip, port);
                dismiss();
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.5);
        lp.height = (int) (d.heightPixels * 0.35);
        dialogWindow.setAttributes(lp);

        // 将背景设置为透明
        getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
    }

    private void initColor() {
        GradientDrawable drawable = (GradientDrawable) mToolbar.getBackground();
        drawable.setColor(Color.parseColor(themeColor));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable btnDrawable = (RippleDrawable) mSure.getBackground();
            if (btnDrawable != null) {
                GradientDrawable maskDrawable = (GradientDrawable) btnDrawable.getDrawable(0);
                if (maskDrawable != null) {
                    maskDrawable.setColor(Color.parseColor(themeColor));
                }
            }
        } else {
            GradientDrawable btnDrawable = (GradientDrawable) mSure.getBackground();
            drawable.setColor(Color.parseColor(themeColor));
        }
    }

    // 端口回调
    public interface AddressCallback {
        void onAddAddress(String ip, String port);
    }
}
