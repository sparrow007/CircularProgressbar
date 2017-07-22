package com.jackandphantom.circularprogressbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import android.view.View;
import android.view.animation.DecelerateInterpolator;




public class CircleProgressbar extends View {


    private Paint innerCircle = new Paint();
    private Paint outerCircle = new Paint();

    private int width, height;
    private final int DEFAULT_FOREGROUND_PROGRESS_WIDTH = 10;
    private final int DEFAULT_BACKGROUND_CIRCLE_WIDTH = 10;
    private RectF rectF = new RectF();

    private int backgroundProgressWidth;
    private int foregroundProgressWidth;
    private int backgroundProgressColor;
    private int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.GRAY;
    private int foregroundProgressColor;
    private int DEFAULT_FOREGROUND_PROGRESS_COLOR = Color.BLACK;

    private float progress = 0;
    private int   startAngle = -90;
    private float sweepAngle = 0;

    private int centerPoint;
    private int drawRadius, drawOuterRadius;
    private int DEFAULT_ANIMATION_DURATION = 1500;

    public CircleProgressbar(Context context) {
        super(context);
        init();
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    /* TypedArray used to getting all the values from xml */
    public CircleProgressbar(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar,0,0);
        backgroundProgressWidth = a.getInteger(R.styleable.CircleProgressbar_cpb_backgroundProgressWidth, DEFAULT_BACKGROUND_CIRCLE_WIDTH);
        foregroundProgressWidth = a.getInteger(R.styleable.CircleProgressbar_cpb_foregroundProgressWidth, DEFAULT_FOREGROUND_PROGRESS_WIDTH);
        backgroundProgressColor = a.getColor(R.styleable.CircleProgressbar_cpb_backgroundProgressColor, DEFAULT_BACKGROUND_PROGRESS_COLOR);
        foregroundProgressColor = a.getColor(R.styleable.CircleProgressbar_cpb_foregroundProgressColor, DEFAULT_FOREGROUND_PROGRESS_COLOR);
        this.progress                = a.getFloat(R.styleable.CircleProgressbar_cpb_progress, progress);

        a.recycle();
        init();

    }

    /* initialize paint object for drawing shapes  */
    private void init() {


        innerCircle.setStrokeWidth(foregroundProgressWidth);
        innerCircle.setAntiAlias(true);
        innerCircle.setStyle(Paint.Style.STROKE);
        innerCircle.setColor(foregroundProgressColor);

        outerCircle.setStrokeWidth(backgroundProgressWidth );
        outerCircle.setAntiAlias(true);
        outerCircle.setColor(backgroundProgressColor);
        outerCircle.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        sweepAngle =  (360 * progress / 100);
        canvas.drawCircle(centerPoint, centerPoint, drawRadius, outerCircle);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, innerCircle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        centerPoint = Math.min(width , height);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        setRadiusRect();
    }

    /* getting the bounds of both background and foreground circle */
    private void setRadiusRect() {
        centerPoint = Math.min(width, height) /2 ;
        int subtractingValue = (backgroundProgressWidth > foregroundProgressWidth) ? backgroundProgressWidth : foregroundProgressWidth;
        int newSeekWidth = subtractingValue / 2;
        drawRadius = Math.min((width - subtractingValue)/ 2, (height - subtractingValue)/2);
        drawOuterRadius=  Math.min((width - newSeekWidth), (height - newSeekWidth));
        rectF.set(subtractingValue /2, subtractingValue /2, drawOuterRadius, drawOuterRadius);
    }

    /* All getter and setter region */
    public void setBackgroundProgressWidth(int width) {
        this.backgroundProgressWidth = width;
        outerCircle.setStrokeWidth(backgroundProgressWidth);
        requestLayout();
        invalidate();
    }

    public int getBackgroundProgressWidth() {
        return this.backgroundProgressWidth;
    }

    public void setForegroundProgressWidth(int width) {
        this.foregroundProgressWidth = width;
        innerCircle.setStrokeWidth(foregroundProgressWidth);
        requestLayout();
        invalidate();
    }

    public int getForegroundProgressWidth() {
        return this.foregroundProgressWidth;
    }


    public void setBackgroundProgressColor(int color) {
        this.backgroundProgressColor = color;
        outerCircle.setColor(color);
        requestLayout();
        invalidate();
    }

    public int getBackgroundProgressColor() {
        return this.backgroundProgressColor;
    }

    public void setForegroundProgressColor(int color) {
        this.foregroundProgressColor = color;
        innerCircle.setColor(color);
        requestLayout();
        invalidate();
    }

    public int getForegroundProgressColor() {
        return this.foregroundProgressColor;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress<=100) ? progress : 100;

        invalidate();
    }

    /*Adding the animation to progressbar*/
    public void setProgressWithAnimation(float progress) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

    }

    public void setProgressWithAnimation(float progress, int duration) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

    }

}
