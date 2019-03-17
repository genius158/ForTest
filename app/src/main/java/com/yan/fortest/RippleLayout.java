package com.yan.fortest;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class RippleLayout extends ViewGroup implements View.OnLayoutChangeListener {
  private static final int DEFAULT_COLOR = Color.parseColor("#24000000");

  private static final String TAG = "RippleLayout";
  private View child;
  private Drawable rippleDrawable;
  private int rippleColor;
  private int rippleMaskId;

  public RippleLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RippleLayout);
    rippleColor = ta.getColor(R.styleable.RippleLayout_rippleColor, DEFAULT_COLOR);
    rippleMaskId = ta.getResourceId(R.styleable.RippleLayout_rippleMask, -1);
    ta.recycle();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (getChildCount() > 1) {
      throw new IllegalStateException("can only hold one child");
    }

    child = getChildAt(0);
    if (child == null) {
      throw new IllegalStateException("need a child to dell logic");
    }
  }

  @Override public void setClickable(boolean clickable) {
    super.setClickable(false);
  }

  @Override public void setOnClickListener(final View.OnClickListener onClickListener) {
    child.setOnClickListener(onClickListener);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    child.addOnLayoutChangeListener(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    child.removeOnLayoutChangeListener(this);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
    setMeasuredDimension(MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY));
    Log.e(TAG, "onMeasure: " + getMeasuredWidth() + "  " + getMeasuredHeight());
  }

  @Override protected boolean checkLayoutParams(LayoutParams p) {
    return p instanceof MarginLayoutParams;
  }

  @Override protected LayoutParams generateDefaultLayoutParams() {
    return new MarginLayoutParams(-1, -1);
  }

  @Override protected LayoutParams generateLayoutParams(LayoutParams p) {
    return new MarginLayoutParams(p);
  }

  @Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(), attrs);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    Log.e(TAG, "onLayout: " + l + "  " + t + "  " + r + "  " + b);
    child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
  }

  @Override
  public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
      int oldTop, int oldRight, int oldBottom) {

    boolean bgChanged = rippleDrawable != child.getBackground()
        || child.getBackground() == null && rippleDrawable == null;
    if (bgChanged) {
      Drawable drawableBG = child.getBackground();
      Drawable drawableMask =
          rippleMaskId == -1 ? drawableBG : ContextCompat.getDrawable(getContext(), rippleMaskId);
      rippleDrawable = getSelectableDrawableFor(drawableBG, drawableMask, rippleColor);
      child.setBackground(rippleDrawable);
    }
    Log.e(TAG, "onLayoutChange: " + bgChanged + "   " + rippleDrawable);
  }

  @NonNull public static Drawable getSelectableDrawableFor(Drawable drawable) {
    return getSelectableDrawableFor(drawable, drawable, DEFAULT_COLOR);
  }

  @NonNull
  public static Drawable getSelectableDrawableFor(Drawable drawable, Drawable mask, int color) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      ColorStateList pressedColor = ColorStateList.valueOf(color);
      return new RippleDrawable(pressedColor, drawable, mask);
    }
    return drawable;
  }
}
