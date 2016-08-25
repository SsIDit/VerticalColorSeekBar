package com.androidbro.verticalcolorseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by serg on 10.08.16.
 */
public class VerticalColorSeekBar extends SeekBar {

    public static final String LOG_TAG = VerticalColorSeekBar.class.getName();

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    private OnColorChangedListener mOnColorChangedListener;
    /**
     * The angle by which the SeekBar view should be rotated.
     */
    private static final int ROTATION_ANGLE = -90;

    /**
     * A change listener registrating start and stop of tracking. Need an own listener because the listener in SeekBar
     * is private.
     */
    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    /**
     * Standard constructor to be implemented for all views.
     *
     * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
     * @see android.view.View#View(Context)
     */
    public VerticalColorSeekBar(final Context context) {
        super(context);
        setThumb(null);
    }

    /**
     * Standard constructor to be implemented for all views.
     *
     * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see android.view.View#View(Context, AttributeSet)
     */
    public VerticalColorSeekBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setThumb(null);
    }

    /**
     * Standard constructor to be implemented for all views.
     *
     * @param context  The Context the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the view.
     * @param defStyle An attribute in the current theme that contains a reference to a style resource that supplies default
     *                 values for the view. Can be 0 to not look for defaults.
     * @see android.view.View#View(Context, AttributeSet, int)
     */
    public VerticalColorSeekBar(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setThumb(null);
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    protected final void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
        super.onSizeChanged(height, width, oldHeight, oldWidth);
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    protected final synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    protected final void onDraw(@NonNull final Canvas c) {
        c.rotate(ROTATION_ANGLE);
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    public final void setOnSeekBarChangeListener(final OnSeekBarChangeListener listener) {
        // Do not use super for the listener, as this would not set the fromUser flag properly
        mOnSeekBarChangeListener = listener;
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    public final boolean onTouchEvent(@NonNull final MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setProgressInternally(getMax() - (int) (getMax() * event.getY() / getHeight()), true);
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStartTrackingTouch(this);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                setProgressInternally(getMax() - (int) (getMax() * event.getY() / getHeight()), true);
                break;

            case MotionEvent.ACTION_UP:
                setProgressInternally(getMax() - (int) (getMax() * event.getY() / getHeight()), true);
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                }
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Set the progress by the user. (Unfortunately, Seekbar.setProgressInternally(int, boolean) is not accessible.)
     *
     * @param progress the progress.
     * @param fromUser flag indicating if the change was done by the user.
     */
    public final void setProgressInternally(final int progress, final boolean fromUser) {
        if (progress != getProgress()) {
            super.setProgress(progress);

            if (mOnSeekBarChangeListener != null) {
                mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
            }
        }
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    /*
     * (non-Javadoc) ${see_to_overridden}
     */
    @Override
    public final void setProgress(final int progress) {
        setProgressInternally(progress, false);
    }

    protected int pickColor(int progress) {
        int r = 0;
        int g = 0;
        int b = 0;

        if (progress < 256) {
            b = progress;
        } else if (progress < 256 * 2) {
            g = progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 3) {
            g = 255;
            b = progress % 256;
        } else if (progress < 256 * 4) {
            r = progress % 256;
            g = 256 - progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 5) {
            r = 255;
            g = 0;
            b = progress % 256;
        } else if (progress < 256 * 6) {
            r = 255;
            g = progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 7) {
            r = 255;
            g = 255;
            b = progress % 256;
        }
        return Color.argb(255, r, g, b);
    }

    protected void setupGradient() {
        setMax(256 * 7 - 1);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
                new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                        0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF});
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setStroke((int) convertDpToPixel(2, getContext()), Color.BLACK);
        setProgressDrawable(drawable);

        setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int color = pickColor(progress);
                    ((GradientDrawable) getProgressDrawable()).setStroke((int) convertDpToPixel(1, getContext()), color);
                    if (mOnColorChangedListener != null) {
                        mOnColorChangedListener.onColorChanged(color);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected float convertDpToPixel(float dp, Context context) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupGradient();
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        mOnColorChangedListener = onColorChangedListener;
    }
}