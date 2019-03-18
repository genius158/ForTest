package com.yan.fortest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author genius158
 */
class DrawableDownApi21 extends Drawable {
  private int color;

  private Drawable original;
  private Bitmap cover;
  private Rect bounds = new Rect();

  DrawableDownApi21(Drawable original, int color) {
    this.original = original;
    this.color = color;
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (original != null) {
      original.draw(canvas);
    }
    if (cover != null) {
      canvas.drawBitmap(cover, null, original != null ? original.getBounds() : bounds, null);
    }
  }

  @Override public void setAlpha(int alpha) {

  }

  @Override public void setColorFilter(ColorFilter colorFilter) {

  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override protected boolean onStateChange(int[] stateSet) {
    boolean enabled = false;
    boolean pressed = false;
    boolean focused = false;
    boolean hovered = false;

    for (int state : stateSet) {
      if (state == android.R.attr.state_enabled) {
        enabled = true;
      } else if (state == android.R.attr.state_focused) {
        focused = true;
      } else if (state == android.R.attr.state_pressed) {
        pressed = true;
      } else if (state == android.R.attr.state_hovered) {
        hovered = true;
      }
    }

    if (enabled && (pressed || focused || hovered)) {
      if (cover == null) {
        cover = getBitmap(original, color);
      }
    } else {
      if (cover != null) {
        cover.recycle();
        cover = null;
      }
    }

    invalidateSelf();
    return super.onStateChange(stateSet);
  }

  @Override protected void onBoundsChange(Rect bounds) {
    if (this.bounds.equals(bounds)) {
      return;
    }
    this.bounds.set(bounds);
    if (original != null) {
      original.setBounds(this.bounds);
    }
    invalidateSelf();
  }

  private Bitmap getBitmap(Drawable drawable, int color) {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    Bitmap mask = Bitmap.createBitmap(bounds.right - bounds.left, bounds.bottom - bounds.top,
        Bitmap.Config.ALPHA_8);
    Canvas canvas = new Canvas(mask);
    if (drawable != null) {
      drawable.setBounds(bounds);
      drawable.draw(canvas);
    }
    canvas.drawColor(color, PorterDuff.Mode.DST_IN);
    return mask;
  }

  @Override public boolean isStateful() {
    return true;
  }
}
