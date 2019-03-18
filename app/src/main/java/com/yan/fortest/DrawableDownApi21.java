package com.yan.fortest;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * @author genius158
 */
class DrawableDownApi21 extends Drawable implements Drawable.Callback {
  private int color;

  private Drawable original;
  private Drawable cover;
  private boolean coverShow;
  private Rect bounds = new Rect();

  DrawableDownApi21(Drawable original, int color) {
    this.original = original;
    this.color = color;
    if (original != null) {
      original.setCallback(this);
    }
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (original != null) {
      original.draw(canvas);
    }
    if (!coverShow || cover == null) {
      return;
    }
    cover.draw(canvas);
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
      cover = null;
    }

    invalidateSelf();
    return super.onStateChange(stateSet);
  }

  private void coverBitmap(Drawable original, int color) {
    if (cover != null || original instanceof StateListDrawable) {
      return;
    }
    if (original instanceof ShapeDrawable
        || original instanceof GradientDrawable
        || original instanceof NinePatchDrawable
        || original instanceof BitmapDrawable) {
      ConstantState cs = original.getConstantState();
      if (cs != null) {
        cover = tintDrawable(cs.newDrawable(), ColorStateList.valueOf(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          cover.setLayoutDirection(original.getLayoutDirection());
        }
        cover.setLevel(original.getLevel());
      }
    }

    if (cover == null) {
      cover = new ColorDrawable(color);
    }
    cover.setCallback(this);
    cover.setBounds(bounds);
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

  @Override public boolean isStateful() {
    return true;
  }

  private Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
    final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
    DrawableCompat.setTintList(wrappedDrawable, colors);
    return wrappedDrawable;
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
