package com.yan.fortest;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentAdapter extends PagerAdapter {
  private static final String TAG = "FragmentAdapter";
  private static final boolean DEBUG = false;
  private final FragmentManager mFragmentManager;
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem = null;

  public FragmentAdapter(FragmentManager fm) {
    this.mFragmentManager = fm;
  }

  public abstract Fragment getItem(int var1);

  @Override public void startUpdate(@NonNull ViewGroup container) {
    if (container.getId() == -1) {
      throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
    }
  }

  @Override

  @NonNull public Object instantiateItem(@NonNull ViewGroup container, int position) {
    if (this.mCurTransaction == null) {
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    }

    long itemId = this.getItemId(position);
    String name = makeFragmentName(container.getId(), itemId);
    Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
    if (fragment != null) {
      this.mCurTransaction.attach(fragment);
    } else {
      fragment = this.getItem(position);
      this.mCurTransaction.add(container.getId(), fragment,
          makeFragmentName(container.getId(), itemId));
    }

    if (fragment != this.mCurrentPrimaryItem) {
      final Fragment finalFragment = fragment;
      container.post(new Runnable() {
        @Override public void run() {
          finalFragment.setMenuVisibility(false);
          finalFragment.setUserVisibleHint(false);
        }
      });
    }

    return fragment;
  }

  @Override

  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    if (this.mCurTransaction == null) {
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    }

    this.mCurTransaction.detach((Fragment) object);
  }

  private void setFragmentVisibility(final View view, final Fragment fragment,
      final boolean isVisible) {
    if (fragment == null) {
      return;
    }
    view.post(new Runnable() {
      @Override public void run() {
        fragment.setMenuVisibility(isVisible);
        fragment.setUserVisibleHint(isVisible);
      }
    });
  }

  @Override

  public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    Fragment fragment = (Fragment) object;
    if (fragment != this.mCurrentPrimaryItem) {
      setFragmentVisibility(container, mCurrentPrimaryItem, false);
      setFragmentVisibility(container, fragment, true);
      this.mCurrentPrimaryItem = fragment;
    }
  }

  @Override

  public void finishUpdate(@NonNull ViewGroup container) {
    if (this.mCurTransaction != null) {
      this.mCurTransaction.commitNowAllowingStateLoss();
      this.mCurTransaction = null;
    }
  }

  @Override

  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return ((Fragment) object).getView() == view;
  }

  @Override

  public Parcelable saveState() {
    return null;
  }

  @Override

  public void restoreState(Parcelable state, ClassLoader loader) {
  }

  public long getItemId(int position) {
    return (long) position;
  }

  public static String makeFragmentName(int viewId, long id) {
    return "android:switcher:" + viewId + ":" + id;
  }
}

