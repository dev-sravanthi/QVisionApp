package util;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class ScrollTextView extends MyCustomTextView {
    private Scroller mSlr;
    private int mRndDuration = 24000;
    private int mXPaused = 0;
    private boolean mPaused = true;

    public ScrollTextView(Context context) {
        this(context, null);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public void startScroll() {
        mXPaused = -1 * getWidth();
        mPaused = true;
        resumeScroll();
    }

    public void resumeScroll() {

        if (!mPaused)
            return;
        setHorizontallyScrolling(true);
        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = scrollingLen - (getWidth() + mXPaused);
        int duration = (new Double(mRndDuration * distance * 1.00000
                / scrollingLen)).intValue();

        setVisibility(VISIBLE);
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth();
        rect = null;
        return scrollingLen;
    }

    public void pauseScroll() {
        if (null == mSlr)
            return;

        if (mPaused)
            return;

        mPaused = true;
        mXPaused = mSlr.getCurrX();

        mSlr.abortAnimation();
    }

    @Override

    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr) return;

        if (mSlr.isFinished() && (!mPaused)) {
            this.startScroll();
        }
    }

    public int getRndDuration() {
        return mRndDuration;
    }

    public void setRndDuration(int duration) {
        this.mRndDuration = duration;
    }

    public boolean isPaused() {
        return mPaused;
    }
}

