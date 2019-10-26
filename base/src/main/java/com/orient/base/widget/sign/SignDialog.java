package com.orient.base.widget.sign;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    // 路径名称
    private String path;
    // 签署还是显示图片的标记位
    private boolean isSign;
    private String name;

    private SignFinishCallback mCallback;

    public SignDialog(@NonNull Context context, int themeId, SignFinishCallback callback) {
        super(context, themeId);
        this.mCallback = callback;
    }

    /**
     * 设置展示的路径
     * @param path 路径
     */
    public final void setShowPath(String path) {
        this.path = path;
        this.isSign = false;
    }

    /**
     * 设置签名路径
     * @param parentPath 签名的父路径
     * @param name       文件名
     */
    public final void setSignPath(String parentPath, String name) {
        this.path = parentPath;
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
        lp.height = (int) ((double)d.heightPixels * 0.6 + +UiUtils.dip2px(104f));
        dialogWindow.setAttributes(lp);

        // 将北京设置为透明
        getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
    }

    private void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 存储照片
                Bitmap b = mSignContent.getPaintBitmap();
                String p = PhotoUtils.saveSign(b, path, name);
                if (!TextUtils.isEmpty(p)) {
                    mCallback.onFinish(true, p);
                }
            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onFinish(false, null);
                dismiss();
            }
        });
    }

    private void initWidget() {
        mBtnYes = findViewById(R.id.mBtnYes);
        mBtnNo = findViewById(R.id.mBtnNo);
        mShowContent = findViewById(R.id.iv_content);
        mSignContent = findViewById(R.id.pt);

        if (isSign) {
            // 是否是签名
            mSignContent.setVisibility(View.VISIBLE);
        } else {
            mShowContent.setVisibility(View.VISIBLE);
            mShowContent.setImageBitmap(PhotoUtils.getSingleBitmapByPath(path));
        }
    }

    /**
     * 签署信息的回调
     */
    public interface SignFinishCallback {
        void onFinish(boolean isFinish, String p);
    }
}
