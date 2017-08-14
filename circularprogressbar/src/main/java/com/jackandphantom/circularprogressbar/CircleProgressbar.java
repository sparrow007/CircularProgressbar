package com.jackandphantom.circularprogressbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


public class CircleProgressbar extends View {

    /* innerCircle  is used to draw reference surface on which progress circle is drawn and move */
    private Paint innerCircle = new Paint();

    /* outerCircle is used for progress circle which progress your status*/
    private Paint outerCircle = new Paint();

    /*widht and height is used to get view's width and height*/
    private int width, height;

    /* These two values is used as a default value for foreground (progress) circle
     *  and other is used for background circle width how much width will both circle's
    */
    private final int DEFAULT_FOREGROUND_PROGRESS_WIDTH = 10;
    private final int DEFAULT_BACKGROUND_CIRCLE_WIDTH = 10;

    /* rectF is used for make progress circle which is basically a arc so it's contain the bounds of arc */
    private RectF rectF = new RectF();

    /* this boolean value is for touch event is move  */
    private boolean moveCorrect;

    /* backgroundProgressWidth is used for background progress width */
    private int backgroundProgressWidth;

    /* foregroundProgressWidth is used for foreground progress width */
    private int foregroundProgressWidth;

    /* backgroundProgressColor is used for background progress color */
    private int backgroundProgressColor;

    /* This is the default value of background progress color
     */
    private int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.GRAY;
    /* These two variables are used for foreground progess color
   * if no color is specified by the user then for default we use these variables
      * */
    private int foregroundProgressColor;
    private int DEFAULT_FOREGROUND_PROGRESS_COLOR = Color.BLACK;

    /*Progess is used for progress in progressbar*/
    private float progress = 0;

    /* start angle is used where the progress should start so according to android it will -90 */
    private int   startAngle = -90;

    /* sweep angle is used for progress angle in progress bar*/
    private float sweepAngle = 0;

    /* max progress as name implies this is used for finding the max value for progress
    * default it's value is 100
    * */
    private float maxProgress = 100;

    /*centerPoint is used for findiding the smallest value between width and height of view */
    private int centerPoint;

    /* subtractingValues is used to find which one is bigger among the background circle and foreground circle */
    private int subtractingValue;

    /* As the name says this will used for checking that progress start from clockwise or counter clockwise */
    private boolean clockWise;
    private int drawRadius, drawOuterRadius;

    /* This is default timing of animation which is used to progresss the progress bar */
    private int DEFAULT_ANIMATION_DURATION = 1500;

    /*This name says that progress bar should be touchable or not*/
    private boolean isTouchEnabled = false;
    private boolean roundedCorner;

    private  OnProgressbarChangeListener onProgressbarChangeListener;

    public CircleProgressbar(Context context) {
        super(context);
        init();
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    /* TypedArray used to getting all the values from xml (user) */
    public CircleProgressbar(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar,0,0);
        backgroundProgressWidth = a.getInteger(R.styleable.CircleProgressbar_cpb_backgroundProgressWidth, DEFAULT_BACKGROUND_CIRCLE_WIDTH);
        foregroundProgressWidth = a.getInteger(R.styleable.CircleProgressbar_cpb_foregroundProgressWidth, DEFAULT_FOREGROUND_PROGRESS_WIDTH);
        backgroundProgressColor = a.getColor(R.styleable.CircleProgressbar_cpb_backgroundProgressColor, DEFAULT_BACKGROUND_PROGRESS_COLOR);
        foregroundProgressColor = a.getColor(R.styleable.CircleProgressbar_cpb_foregroundProgressColor, DEFAULT_FOREGROUND_PROGRESS_COLOR);
        this.progress                = a.getFloat(R.styleable.CircleProgressbar_cpb_progress, progress);
        this.roundedCorner      = a.getBoolean(R.styleable.CircleProgressbar_cpb_roundedCorner, false);
        this.clockWise      = a.getBoolean(R.styleable.CircleProgressbar_cpb_clockwise, false);
        this.isTouchEnabled      = a.getBoolean(R.styleable.CircleProgressbar_cpb_touchEnabled, false);


        a.recycle();
        init();
        if (roundedCorner) {
            setRoundedCorner(roundedCorner);
        }
        if (this.progress > 0) {
            setProgress(this.progress);
        }

        if (clockWise) {
            setClockwise(clockWise);
        }

        if (isTouchEnabled) {
            enabledTouch(isTouchEnabled);
        }
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


    /*Here we draw two things first circle you can say this is surface on which arc means progress will drawn*/
    @Override
    protected void onDraw(Canvas canvas) {



        canvas.drawCircle(centerPoint, centerPoint, drawRadius, outerCircle);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, innerCircle);
        super.onDraw(canvas);
    }

    /* This is callback method which find how much size will assign to this child view */
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
        subtractingValue = (backgroundProgressWidth > foregroundProgressWidth) ? backgroundProgressWidth : foregroundProgressWidth;
        int newSeekWidth = subtractingValue / 2;
        drawRadius = Math.min((width - subtractingValue)/ 2, (height - subtractingValue)/2);
        drawOuterRadius=  Math.min((width - newSeekWidth), (height - newSeekWidth));
        rectF.set(subtractingValue /2, subtractingValue /2, drawOuterRadius, drawOuterRadius);
    }

  /* This is touchEvent callback is active when user is need this for getting the touch event in his app so
    * he need to make true to touch event
      * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isTouchEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (onProgressbarChangeListener != null)
                        onProgressbarChangeListener.onStartTracking(this);
                    checkForCorrect(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (moveCorrect)
                        justMove(event.getX(), event.getY());
                    upgradeProgress(this.progress, true);

                    break;
                case MotionEvent.ACTION_UP:
                    if (onProgressbarChangeListener != null)
                        onProgressbarChangeListener.onStopTracking(this);
                    moveCorrect = false;
                    break;
            }

            return true;
        }
        return false;
    }
/* update progress is used when setProgress is used so first this method upadate the value of progress and then

 * update the value of sweepangle and according to clockwise it's also decides that sweep
 *
  * andle should be positive or negative
  * */
    private void upgradeProgress(float progress, boolean b) {
        this.progress = (progress<=maxProgress) ? progress : maxProgress;
        sweepAngle =  (360 * progress / maxProgress);

        if (this.clockWise) {
            if (sweepAngle > 0) {
                sweepAngle = -sweepAngle;
            }
        }
        if (onProgressbarChangeListener != null)
            onProgressbarChangeListener.onProgressChanged(this, progress, b);
        invalidate();

    }

    /*when the user make touchevent true then this method will be called and it's works is to incress and decress the

    * sweep value so it directly increases and dicreseas the values of progress bar
    * */

    private void justMove(float x, float y) {

        if (clockWise) {
            float degree = (float) Math.toDegrees(Math.atan2(x - centerPoint, centerPoint - y ));
            if (degree > 0) {
                degree -= 360;
            }
            sweepAngle = degree;
        }
        else  {
            float degree = (float) Math.toDegrees(Math.atan2(x - centerPoint, centerPoint - y ));
            if (degree < 0) {
                degree += 360;
            }

            sweepAngle = degree;
        }

        progress = (sweepAngle * maxProgress / 360);

       /* float degree = (float) Math.toDegrees(Math.atan2(x - centerPoint, centerPoint - y ));
        if (degree < 0) {
            degree += 360;
        }

             sweepAngle = degree;*/
        invalidate();

    }

    /* This method is also used by touchevent so it's just find that user click on circle or not */
    private void checkForCorrect(float x, float y) {

        float distance = (float) Math.sqrt(Math.pow((x - centerPoint), 2) + Math.pow((y - centerPoint),2));
        if (distance < drawOuterRadius/2 + subtractingValue && distance > drawOuterRadius/2 - subtractingValue*2 ) {


            moveCorrect = true;
            if (clockWise) {
                float degree = (float) Math.toDegrees(Math.atan2(x - centerPoint, centerPoint - y ));

                if (degree > 0) {
                    degree -= 360;
                }

                sweepAngle = degree;
            }
            else  {
                float degree = (float) Math.toDegrees(Math.atan2(x - centerPoint, centerPoint - y ));
                if (degree < 0) {
                    degree += 360;
                }

                sweepAngle = degree;
            }




            progress = (sweepAngle * maxProgress / 360);

            invalidate();
        }



    }

    public void setOnProgressbarChangeListener(OnProgressbarChangeListener onProgressbarChangeListener) {
        this.onProgressbarChangeListener = onProgressbarChangeListener;
    }

    /* This is interface for informing about the progress */

    public interface OnProgressbarChangeListener {
        void onProgressChanged(CircleProgressbar circleSeekbar, float progress, boolean fromUser);
        void onStartTracking(CircleProgressbar circleSeekbar);
        void onStopTracking(CircleProgressbar circleSeekbar);
    }

    /* All getter and setter region */
    public void setClockwise(boolean clockwise) {
        this.clockWise = clockwise;
        if (this.clockWise) {
            if (sweepAngle > 0) {
                sweepAngle = -sweepAngle;
            }
        }
        invalidate();
    }

    public boolean isClockWise() {
        return this.clockWise;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    public float getMaxProgress() {
        return this.maxProgress;
    }

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
        upgradeProgress(progress, false);

    }

    public void enabledTouch (boolean enabled) {
        this.isTouchEnabled = enabled;
        invalidate();
    }

    public boolean isTouchEnabled() {
        return this.isTouchEnabled;
    }

    public void setRoundedCorner(boolean roundedCorner) {
        if (roundedCorner) {
            innerCircle.setStrokeCap(Paint.Cap.ROUND);
            outerCircle.setStrokeCap(Paint.Cap.ROUND);
        }
        else {
            innerCircle.setStrokeCap(Paint.Cap.SQUARE);
            outerCircle.setStrokeCap(Paint.Cap.SQUARE);
        }
        invalidate();
    }

    public boolean isRoundedCorner() {
        return this.roundedCorner;
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

