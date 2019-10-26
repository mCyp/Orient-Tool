package com.orient.tool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.orient.base.utils.PhotoUtils;
import com.orient.base.widget.sign.SignDialog;

public class MainActivity extends AppCompatActivity {

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
                        content.setImageBitmap(PhotoUtils.getSingleBitmapByPath(p));
                    }
                });
                dialog.setSignPath(getExternalCacheDir().getAbsolutePath(), "144");
                dialog.show();
            }
        });
    }
}
