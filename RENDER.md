# 界面绘制FAQ

## 单刀直入
布局
|                                |爷爷grandpa(FrameLayout)|                                 |
|-------------------------------:|:----------------------:|:--------------------------------|
| 父亲father(FrameLayout)         |                        |叔叔uncle(FragmentLayout)设置了背景| 
| 儿子child(TextView)             |                        |                                |   
|                                 |                       |                                | 

child:      
宽match_parent child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |     1   |    1    |    0    |      0       |    0    |
| parent（父亲） |     1   |    1    |    0    |      0       |    0    |
| grandpa（爷爷）|     1   |    1    |    0    |      0       |    0    |
| uncle（叔叔）  |     0   |    0    |    0    |      0       |     0   |

宽match_parent child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |     0   |    0    |    1    |      1       |    1    |
| parent（父亲） |     0   |    0    |    0    |      0       |    0    |
| grandpa（爷爷）|     0   |    0    |    0    |      0       |    0    |
| uncle（叔叔）  |     0   |    0    |    0    |      0       |    0    |

宽wrap_content child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |    1    |    1    |    0    |      0       |   0     |
| parent（父亲） |    1    |    1    |    0    |      0       |   0     |
| grandpa（爷爷）|    1    |    1    |    0    |      0       |   0     |
| uncle（叔叔）  |    0    |    0    |    0    |      0       |   0     |

宽wrap_content child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子） |     0    |    0    |    1    |      1       |    1    |
| parent（父亲） |    0    |    0    |    0    |      0       |    0    |
| grandpa（爷爷）|    0    |    0    |    0    |      0       |    0    |
| uncle（叔叔）  |    0    |    0    |    0    |      0       |    0    |


不可见的child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |    0    |    0    |   0     |      0       |    0    |
| parent（父亲） |    1    |    1    |   0     |      0       |    0    |
| grandpa（爷爷）|    1    |    1    |   0     |      0       |    0    |
| uncle（叔叔）  |    0    |    0    |   0     |      0       |    0    |

不可见的child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |     0   |    0    |    0    |      0       |    0    |
| parent（父亲） |     0   |    0    |    0    |      0       |    0    |
| grandpa（爷爷）|     0   |    0    |    0    |      0       |    0    |
| uncle（叔叔）  |     0   |    0    |    0    |      0       |    0    |

parent（父亲）:      
宽match_parent parent 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)   |     0    |   0     |    0    |       0      |    0    |
| parent（父亲） |    1    |    1    |     0   |       0      |    0    |
| grandpa（爷爷）|    1    |    1    |     0   |       0      |    0    |
| uncle（叔叔）  |    0    |    0    |     0   |       0      |    0    |

宽match_parent parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |   0     |   0     |   0     |      0       |   0     |
| parent（父亲） |     0   |     0   |    1    |       1      |   1     |
| grandpa（爷爷）|     0   |     0   |   0     |       0      |    0    |
| uncle（叔叔）  |     0   |     0   |    0    |     0        |   0     |

宽wrap_content parent 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |  0      |    0    |   0     |     0        |   0     |
| parent（父亲） |    1    |      1  |     0   |       0      |     0   |
| grandpa（爷爷）|    1    |      1  |     0   |       0      |     0   |
| uncle（叔叔）  |    0    |      0  |     0   |       0      |     0   |

宽wrap_content parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |    0    |   0     |   0     |     0        |    0    |
| parent（父亲） |      0  |     0   |     0   |       1      |      0  |
| grandpa（爷爷）|      0  |     0   |     0   |       0      |      0  |
| uncle（叔叔）  |      0  |     0   |     0   |       0      |      0  |

不可见的parent 调用requestLayout (当前情况下，child调用呢?)
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |   0     |   0     |    0    |     0        |   0     |
| parent（父亲） |     0   |     0   |      0  |       0      |     0   |
| grandpa（爷爷）|     1   |     1   |      0  |       0      |     0   |
| uncle（叔叔）  |     0   |     0   |      0  |       0      |     0   |

不可见的parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |   0     |   0     |    0    |     0        |   0     |
| parent（父亲） |     0   |     0   |      0  |       0      |     0   |
| grandpa（爷爷）|     0   |     0   |      0  |       0      |     0   |
| uncle（叔叔）  |     0   |     0   |      0  |       0      |     0   |

如果是match_parent textView setText（）     
如果是wrap_content textView setText（）     
如果是match_parent textView setText（）出现换行的情况     
只调用child的api 什么情况下uncle会触发onMeasure、 onLayout    

番外:怎么监听setVisibility(GONE)     


### 代码提示
```java

protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    ...
     child.getVisibility() != GONE;
    ...
}


 public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(mParent)) {
            Insets insets = getOpticalInsets();
            int oWidth  = insets.left + insets.right;
            int oHeight = insets.top  + insets.bottom;
            widthMeasureSpec  = MeasureSpec.adjust(widthMeasureSpec,  optical ? -oWidth  : oWidth);
            heightMeasureSpec = MeasureSpec.adjust(heightMeasureSpec, optical ? -oHeight : oHeight);
        }

        // Suppress sign extension for the low bytes
        long key = (long) widthMeasureSpec << 32 | (long) heightMeasureSpec & 0xffffffffL;
        if (mMeasureCache == null) mMeasureCache = new LongSparseLongArray(2);

        final boolean forceLayout = (mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT;

        // Optimize layout by avoiding an extra EXACTLY pass when the view is
        // already measured as the correct size. In API 23 and below, this
        // extra pass is required to make LinearLayout re-distribute weight.
        final boolean specChanged = widthMeasureSpec != mOldWidthMeasureSpec
                || heightMeasureSpec != mOldHeightMeasureSpec;
        final boolean isSpecExactly = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY;
        final boolean matchesSpecSize = getMeasuredWidth() == MeasureSpec.getSize(widthMeasureSpec)
                && getMeasuredHeight() == MeasureSpec.getSize(heightMeasureSpec);
        final boolean needsLayout = specChanged
                && (sAlwaysRemeasureExactly || !isSpecExactly || !matchesSpecSize);

        if (forceLayout || needsLayout) {
            // first clears the measured dimension flag
            mPrivateFlags &= ~PFLAG_MEASURED_DIMENSION_SET;

            resolveRtlPropertiesIfNeeded();

            int cacheIndex = forceLayout ? -1 : mMeasureCache.indexOfKey(key);
            if (cacheIndex < 0 || sIgnoreMeasureCache) {
                // measure ourselves, this should set the measured dimension flag back
                onMeasure(widthMeasureSpec, heightMeasureSpec);
                mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
            } else {
                long value = mMeasureCache.valueAt(cacheIndex);
                // Casting a long to int drops the high 32 bits, no mask needed
                setMeasuredDimensionRaw((int) (value >> 32), (int) value);
                mPrivateFlags3 |= PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
            }

            // flag not set, setMeasuredDimension() was not invoked, we raise
            // an exception to warn the developer
            if ((mPrivateFlags & PFLAG_MEASURED_DIMENSION_SET) != PFLAG_MEASURED_DIMENSION_SET) {
                throw new IllegalStateException("View with id " + getId() + ": "
                        + getClass().getName() + "#onMeasure() did not set the"
                        + " measured dimension by calling"
                        + " setMeasuredDimension()");
            }

            mPrivateFlags |= PFLAG_LAYOUT_REQUIRED;
        }

        mOldWidthMeasureSpec = widthMeasureSpec;
        mOldHeightMeasureSpec = heightMeasureSpec;

        mMeasureCache.put(key, ((long) mMeasuredWidth) << 32 |
                (long) mMeasuredHeight & 0xffffffffL); // suppress sign extension
    }

```


```java

void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache,
            boolean fullInvalidate) {
    if (skipInvalidate()) {
             return;
    }
    if ((mPrivateFlags & (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)) == (PFLAG_DRAWN | PFLAG_HAS_BOUNDS)
            || (invalidateCache && (mPrivateFlags & PFLAG_DRAWING_CACHE_VALID) == PFLAG_DRAWING_CACHE_VALID)
            || (mPrivateFlags & PFLAG_INVALIDATED) != PFLAG_INVALIDATED
            || (fullInvalidate && isOpaque() != mLastIsOpaque)) {
        if (fullInvalidate) {
            mLastIsOpaque = isOpaque();
            mPrivateFlags &= ~PFLAG_DRAWN;
        }
        mPrivateFlags |= PFLAG_DIRTY;
        if (invalidateCache) {
            mPrivateFlags |= PFLAG_INVALIDATED;
            mPrivateFlags &= ~PFLAG_DRAWING_CACHE_VALID;
        }
        // Propagate the damage rectangle to the parent view.
        final AttachInfo ai = mAttachInfo;
        final ViewParent p = mParent;
        if (p != null && ai != null && l < r && t < b) {
            final Rect damage = ai.mTmpInvalRect;
            damage.set(l, t, r, b);
            p.invalidateChild(this, damage);
        }
    }
}



 public RenderNode updateDisplayListIfDirty() {
        final RenderNode renderNode = mRenderNode;
        if (!canHaveDisplayList()) {
            // can't populate RenderNode, don't try
            return renderNode;
        }

        if ((mPrivateFlags & PFLAG_DRAWING_CACHE_VALID) == 0
                || !renderNode.hasDisplayList()
                || (mRecreateDisplayList)) {
            // Don't need to recreate the display list, just need to tell our
            // children to restore/recreate theirs
            if (renderNode.hasDisplayList()
                    && !mRecreateDisplayList) {
                mPrivateFlags |= PFLAG_DRAWN | PFLAG_DRAWING_CACHE_VALID;
                mPrivateFlags &= ~PFLAG_DIRTY_MASK;
                dispatchGetDisplayList();

                return renderNode; // no work needed
            }

            // If we got here, we're recreating it. Mark it as such to ensure that
            // we copy in child display lists into ours in drawChild()
            mRecreateDisplayList = true;

            int width = mRight - mLeft;
            int height = mBottom - mTop;
            int layerType = getLayerType();

            final RecordingCanvas canvas = renderNode.beginRecording(width, height);

            try {
                if (layerType == LAYER_TYPE_SOFTWARE) {
                    buildDrawingCache(true);
                    Bitmap cache = getDrawingCache(true);
                    if (cache != null) {
                        canvas.drawBitmap(cache, 0, 0, mLayerPaint);
                    }
                } else {
                    computeScroll();

                    canvas.translate(-mScrollX, -mScrollY);
                    mPrivateFlags |= PFLAG_DRAWN | PFLAG_DRAWING_CACHE_VALID;
                    mPrivateFlags &= ~PFLAG_DIRTY_MASK;

                    // Fast path for layouts with no backgrounds
                    if ((mPrivateFlags & PFLAG_SKIP_DRAW) == PFLAG_SKIP_DRAW) {
                        dispatchDraw(canvas);
                        drawAutofilledHighlight(canvas);
                        if (mOverlay != null && !mOverlay.isEmpty()) {
                            mOverlay.getOverlayView().draw(canvas);
                        }
                        if (debugDraw()) {
                            debugDrawFocus(canvas);
                        }
                    } else {
                        draw(canvas);
                    }
                }
            } finally {
                renderNode.endRecording();
                setDisplayListProperties(renderNode);
            }
        } else {
            mPrivateFlags |= PFLAG_DRAWN | PFLAG_DRAWING_CACHE_VALID;
            mPrivateFlags &= ~PFLAG_DIRTY_MASK;
        }
        return renderNode;
    }



private boolean skipInvalidate() {
        return (mViewFlags & VISIBILITY_MASK) != VISIBLE && mCurrentAnimation == null &&
                (!(mParent instanceof ViewGroup) ||
                        !((ViewGroup) mParent).isViewTransitioning(this));
    }
```


```java
public boolean isLayoutRequested() {
       return (mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT;
   }

public void requestLayout() {
    ...
    mPrivateFlags |= PFLAG_FORCE_LAYOUT;
    if (mParent != null && !mParent.isLayoutRequested()) {
            mParent.requestLayout();
        }
    ...
}

public void layout(int l, int t, int r, int b) {
   ...
   mPrivateFlags &= ~PFLAG_FORCE_LAYOUT; 
   ...
}
```
