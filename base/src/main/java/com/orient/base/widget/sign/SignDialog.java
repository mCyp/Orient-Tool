package com.orient.base.widget.sign;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.orient.base.R;
import com.orient.base.utils.PhotoUtils;
import com.orient.base.utils.UiUtils;

/**
 * 签署的弹出框
 * Author WangJie
 * Created on 2019/10/23.
 */
@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions", "unused"})
public class SignDialog extends Dialog {

    // 展示的签名照片
    private ImageView mShowContent;
    // 签署的内容
    private PaintView mSignContent;
    private Button mBtnYes;
    private Button mBtnNo;
    private Toolbar mToolbar;

    // 签署文件存储的父路径
    private String dir;
    private String name;
    // 显示文件的路径
    private String path;
    private String themeColor;

    // 签署还是显示图片的标记位
    private boolean isSign;

    private SignFinishCallback mCallback;

    public SignDialog(@NonNull Context context, int themeId, SignFinishCallback callback) {
        super(context, themeId);
        this.mCallback = callback;
    }

    public SignDialog(@NonNull Context context, int themeId) {
        super(context, themeId);
    }

    /**
     * 设置回调
     *
     * @param callback 回调
     */
    public void setCallback(SignFinishCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 设置主题色
     *
     * @param color 颜色代码
     */
    public void setThemeColor(String color) {
        themeColor = color;
    }

    private void setColor(View view, int color) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(color);
    }

    /**
     * 设置展示的路径
     *
     * @param path 路径
     */
    public final void setShowPath(String path) {
        this.path = path;
        this.isSign = false;
    }

    /**
     * 设置签名路径
     *
     * @param parentPath 签名的父路径
     * @param name       文件名
     */
    public final void setSignPath(String parentPath, String name) {
        this.dir = parentPath;
        this.name = name;
        this.isSign = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);

        initWidget();
        initListener();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics();
        lp.width = (int) ((double) d.widthPixels * 0.6);
        lp.height = (int) ((double) d.heightPixels * 0.6 + +UiUtils.dip2px(104f));
        dialogWindow.setAttributes(lp);

        // 将背景设置为透明
        getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
    }

    private void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSign) {
                    // 存储照片
                    Bitmap b = mSignContent.getPaintBitmap();
                    String p;
                    if (TextUtils.isEmpty(path)) {
                        p = PhotoUtils.saveSign(b, dir, name);
                    } else {
                        p = PhotoUtils.saveSign(b, path);
                    }
                    path = p;
                    if (!TextUtils.isEmpty(p)) {
                        mCallback.onFinish(true, p);
                    }
                }
                dismiss();
            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSign) {
                    mSignContent.clear();
                } else {
                    isSign = true;
                    // 重新绘制
                    mSignContent.setIsCanSign(true);
                    mSignContent.clear();
                    mSignContent.setVisibility(View.VISIBLE);
                    mShowContent.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initWidget() {
        mBtnYes = findViewById(R.id.mBtnYes);
        mBtnNo = findViewById(R.id.mBtnNo);
        mShowContent = findViewById(R.id.iv_content);
        mSignContent = findViewById(R.id.pt);
        mToolbar = findViewById(R.id.toolbar);

        if (!TextUtils.isEmpty(themeColor))
            initColor();

        if (isSign) {
            // 是否是签名
            mSignContent.setVisibility(View.VISIBLE);
            mShowContent.setVisibility(View.GONE);
        } else {
            mShowContent.setVisibility(View.VISIBLE);
            mSignContent.setVisibility(View.GONE);
            mShowContent.setImageBitmap(PhotoUtils.getSingleBitmapByPath(path));
        }
    }

    private void initColor() {
        GradientDrawable drawable = (GradientDrawable) mToolbar.getBackground();
        drawable.setColor(Color.parseColor(themeColor));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable btnDrawable = (RippleDrawable) mBtnYes.getBackground();
            if (btnDrawable != null) {
                GradientDrawable maskDrawable = (GradientDrawable) btnDrawable.getDrawable(0);
                if (maskDrawable != null) {
                    maskDrawable.setColor(Color.parseColor(themeColor));
                }
            }
        } else {
            GradientDrawable btnDrawable = (GradientDrawable) mBtnYes.getBackground();
            drawable.setColor(Color.parseColor(themeColor));
        }
    }

    /**
     * 签署信息的回调
     */
    public interface SignFinishCallback {
        void onFinish(boolean isFinish, String p);
    }
}
