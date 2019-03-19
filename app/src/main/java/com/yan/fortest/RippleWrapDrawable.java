package com.yan.fortest;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.NonNull;

/**
 * @author genius158
 */
class RippleWrapDrawable extends Drawable implements Drawable.Callback {
  private int color;

  private RippleDrawable original;
  private Drawable mask;

  RippleWrapDrawable(RippleDrawable original, Drawable mask) {
    this.original = original;
    this.mask = mask;
    if (original != null) {
      original.setFilterBitmap(true);
      original.setCallback(this);
    }
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (original != null) {
      original.draw(canvas);
    }
  }

  @Override public void setAlpha(int alpha) {
    if (original != null) {
      original.setAlpha(alpha);
    }
  }

  @Override public void setColorFilter(ColorFilter colorFilter) {
    if (original != null) {
      original.setColorFilter(colorFilter);
    }
  }

  @Override public int getOpacity() {
    return original != null ? original.getOpacity() : PixelFormat.TRANSLUCENT;
  }

  @Override protected boolean onStateChange(int[] stateSet) {
    if (original != null) {
      original.setState(stateSet);
    }
    invalidateSelf();
    return super.onStateChange(stateSet);
  }

  @Override protected void onBoundsChange(Rect bounds) {
    if (original != null) {
      original.setBounds(bounds);
    }

    invalidateSelf();
  }

  @Override public boolean isStateful() {
    return true;
  }

  @Override public boolean setVisible(boolean visible, boolean restart) {
    if (original != null) {
      original.setVisible(visible, restart);
    }
    return super.setVisible(visible, restart);
  }

  @Override public void invalidateDrawable(@NonNull Drawable who) {
    this.invalidateSelf();
  }

  @Override public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
    this.scheduleSelf(what, when);
  }

  @Override public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
    this.unscheduleSelf(what);
  }
}
