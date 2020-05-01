package ir.motevalian.videotrimmer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import ir.motevalian.videotrimmer.R;
import ir.motevalian.videotrimmer.interfaces.OnProgressVideoListener;
import ir.motevalian.videotrimmer.interfaces.OnRangeSeekBarListener;

public class ProgressBarView extends View implements OnRangeSeekBarListener, OnProgressVideoListener {

    private final Paint mBackgroundColor = new Paint();
    private final Paint mProgressColor = new Paint();
    private int mProgressHeight;
    private int mViewWidth;
    private Rect mBackgroundRect;
    private Rect mProgressRect;

    public ProgressBarView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int lineProgress = ContextCompat.getColor(getContext(), R.color.progress_color);
        int lineBackground = ContextCompat.getColor(getContext(), R.color.background_progress_color);

        mProgressHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.progress_video_line_height);

        mBackgroundColor.setAntiAlias(true);
        mBackgroundColor.setColor(lineBackground);

        mProgressColor.setAntiAlias(true);
        mProgressColor.setColor(lineProgress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        mViewWidth = resolveSizeAndState(minW, widthMeasureSpec, 1);

        int minH = getPaddingBottom() + getPaddingTop() + mProgressHeight;
        int viewHeight = resolveSizeAndState(minH, heightMeasureSpec, 1);

        setMeasuredDimension(mViewWidth, viewHeight);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawLineBackground(canvas);
        drawLineProgress(canvas);
    }

    private void drawLineBackground(@NonNull Canvas canvas) {
        if (mBackgroundRect != null) {
            canvas.drawRect(mBackgroundRect, mBackgroundColor);
        }
    }

    private void drawLineProgress(@NonNull Canvas canvas) {
        if (mProgressRect != null) {
            canvas.drawRect(mProgressRect, mProgressColor);
        }
    }

    @Override
    public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    private void updateBackgroundRect(int index, float value) {

        if (mBackgroundRect == null) {
            mBackgroundRect = new Rect(0, 0, mViewWidth, mProgressHeight);
        }

        int newValue = (int) ((mViewWidth * value) / 100);
        if (index == 0) {
            mBackgroundRect = new Rect(newValue, mBackgroundRect.top, mBackgroundRect.right, mBackgroundRect.bottom);
        } else {
            mBackgroundRect = new Rect(mBackgroundRect.left, mBackgroundRect.top, newValue, mBackgroundRect.bottom);
        }

        updateProgress(0, 0, 0.0f);
    }

    @Override
    public void updateProgress(int time, int max, float scale) {

        if (scale == 0) {
            mProgressRect = new Rect(0, mBackgroundRect.top, 0, mBackgroundRect.bottom);
        } else {
            int newValue = (int) ((mViewWidth * scale) / 100);
            mProgressRect = new Rect(mBackgroundRect.left, mBackgroundRect.top, newValue, mBackgroundRect.bottom);
        }

        invalidate();
    }
}
