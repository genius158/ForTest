package com.yan.fortest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

/**
 * @author genius158
 */
class DrawableDownApiAll21 extends Drawable implements Runnable {
  private int color;

  private Drawable original;
  private WeakReference<Bitmap> cover;
  private boolean coverShow;
  private Rect bounds = new Rect();

  DrawableDownApiAll21(Drawable original, int color) {
    this.original = original;
    this.color = color;
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (original != null) {
      original.draw(canvas);
    }
    if (!coverShow) {
      return;
    }
    if (cover == null) {
      return;
    }
    Bitmap coverBitmap = cover.get();
    if (coverBitmap != null) {
      canvas.drawBitmap(coverBitmap, null, original != null ? original.getBounds() : bounds, null);
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

    coverShow = enabled && (pressed || focused || hovered);

    if (coverShow) {
      coverBitmap(original, color);
    } else {
      unscheduleSelf(this);
      scheduleSelf(this, SystemClock.uptimeMillis() + 3000);
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

  private void coverBitmap(Drawable drawable, int color) {
    Bitmap coverBitmap = null;
    if (cover != null) {
      coverBitmap = cover.get();
    }
    if (coverBitmap == null
        || coverBitmap.getWidth() != bounds.width()
        || coverBitmap.getHeight() != bounds.height()) {
      if (coverBitmap != null) {
        coverBitmap.recycle();
      }
      coverBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_4444);
      cover = new WeakReference<>(coverBitmap);
    }
    Canvas canvas = new Canvas(coverBitmap);
    if (drawable != null) {
      drawable.setBounds(bounds);
      drawable.draw(canvas);
    }
    canvas.drawColor(color, PorterDuff.Mode.SRC_IN);
  }

  @Override public boolean isStateful() {
    return true;
  }

  @Override public void run() {
    if (cover != null) {
      Bitmap bitmap = cover.get();
      if (bitmap != null) {
        bitmap.recycle();
      }
      cover.clear();
      cover = null;
    }
  }

  @Override public boolean setVisible(boolean visible, boolean restart) {
    if (!visible) {
      unscheduleSelf(this);
      run();
    }
    return super.setVisible(visible, restart);
  }
}
