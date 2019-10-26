package com.orient.base.widget.sign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 签名版程序
 * @Auther wangjie on 2018/1/10.
 */

@SuppressWarnings("FieldCanBeLocal")
public class PaintView extends View {
    private int mScreenWidth, mScreenHeight;
    private float currentX, currentY;

    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    // 默认可以签名的
    private boolean isCanSign = true;

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context) {
        this(context,null);
    }

    // 初始化基本的参数
    private void init() {
        mPaint = new Paint();
        // 去除锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(mBitmap == null){
            mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.WHITE);
            mPath = new Path();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    public Bitmap getPaintBitmap() {
        return resizeImage(mBitmap, mScreenWidth, mScreenHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isCanSign)
            return true;
        // 这里的想x,y是相当于父容器的
        float x = event.getX();
        float y = event.getY();
        //
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = x;
                currentY = y;
                mPath.moveTo(currentX, currentY);
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = x;
                currentY = y;
                mPath.quadTo(currentX, currentY, x, y);// 画线
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                break;
        }

        // 重新绘制
        invalidate();
        return true;
    }

    public Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        /*float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;*/

        Matrix matrix = new Matrix();
        //matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originWidth, originHeight, matrix, true);
    }

    // 设置当前是否可以做签署的操作
    public void setIsCanSign(Boolean isCanSign) {
        this.isCanSign = isCanSign;
    }


    // 清除画板
    public void clear() {
        if (mCanvas != null) {
            mPath.reset();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

}
