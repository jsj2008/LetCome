package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImage extends ImageView {
    private Paint mStrokePaint;
    
    private int mWidth;
    private int mRadius;
    
    private BitmapShader mBitmapShader;
    private Matrix mMatrix;
    private Paint mBitmapPaint;
    
    public CircularImage(Context context) {
        this(context, null);
    }

    public CircularImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

	public CircularImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MaskedImage);
		int strokeColor = typedArray.getColor(R.styleable.MaskedImage_strokeColor,
				getResources().getColor(R.color.login_head_circle_color));
		float strokeWidth = typedArray.getDimension(
				R.styleable.MaskedImage_strokeWidth, 0.5f);
		typedArray.recycle();

		mStrokePaint = new Paint();
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setColor(strokeColor);
		mStrokePaint.setStrokeWidth(Util.dpToPixel(strokeWidth));
		mStrokePaint.setStyle(Style.STROKE);
	}

    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        Print.i("TAG", "onMeasure");  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
  
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());  
        mRadius = mWidth / 2;  
        setMeasuredDimension(mWidth, mWidth);
    }

	private void setUpShader() {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}

		Bitmap bmp = drawableToBitamp(drawable);
		if(null == bmp){
			return;
		}
		// 将bmp作为着色器，就是在指定区域内绘制bmp
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		float scale = 1.0f;

		// 拿到bitmap宽或高的小值
		int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
		scale = mWidth * 1.0f / bSize;

		// shader的变换矩阵，我们这里主要用于放大或者缩小
		mMatrix.setScale(scale, scale);
		// 设置变换矩阵
		mBitmapShader.setLocalMatrix(mMatrix);
		// 设置shader
		mBitmapPaint.setShader(mBitmapShader);
	}

	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		setUpShader();

		canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
		canvas.drawCircle(mRadius, mRadius, mRadius, mStrokePaint);
		// drawSomeThing(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
    
//    public Bitmap createMask() {
//        int i = getWidth();
//        int j = getHeight();
//        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;//定义Bitmap的质量
//        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
//        Canvas localCanvas = new Canvas(localBitmap);
//        Paint localPaint = new Paint(1);
//        localPaint.setColor(-16777216);
//        float f1 = getWidth();
//        float f2 = getHeight();
//        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
//        localCanvas.drawOval(localRectF, localPaint);
//        return localBitmap;
//    }
}
