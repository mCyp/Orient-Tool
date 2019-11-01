package com.orient.tool;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.orient.base.utils.PhotoUtils;
import com.orient.base.widget.net.NetDialog;
import com.orient.base.widget.sign.SignDialog;

public class MainActivity extends AppCompatActivity {
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView content = findViewById(R.id.iv_content);

        Button button = findViewById(R.id.btn_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SignDialog dialog = new SignDialog(MainActivity.this, R.style.Theme_AppCompat_Dialog, new SignDialog.SignFinishCallback() {
                    @Override
                    public void onFinish(boolean isFinish, String p) {
                        if(isFinish) {
                            path = p;
                            content.setImageBitmap(PhotoUtils.getSingleBitmapByPath(p));
                        }
                    }
                });
                dialog.setThemeColor("#f06292");
                dialog.setSignPath(getExternalCacheDir().getAbsolutePath(), "144");
                dialog.show();

                /*NetDialog dialog = new NetDialog(MainActivity.this, R.style.Theme_AppCompat_Dialog, new NetDialog.AddressCallback() {
                    @Override
                    public void onAddAddress(String ip, String port) {
                        Toast.makeText(MainActivity.this,"ip："+ip+"，port："+port,Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setThemeColor("#f06292");
                dialog.show();*/
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(path))
                    return;
                final SignDialog dialog = new SignDialog(MainActivity.this, R.style.Theme_AppCompat_Dialog, new SignDialog.SignFinishCallback() {
                    @Override
                    public void onFinish(boolean isFinish, String p) {
                        if(isFinish){
                            path = p;
                            content.setImageBitmap(PhotoUtils.getSingleBitmapByPath(p));
                        }
                    }
                });
                dialog.setThemeColor("#f06292");
                dialog.setShowPath(path);
                dialog.show();
            }
        });
    }
}
