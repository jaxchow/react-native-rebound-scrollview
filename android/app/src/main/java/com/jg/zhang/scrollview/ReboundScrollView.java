package com.jg.zhang.scrollview;


import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.MeasureSpecAssertions;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.scroll.OnScrollDispatchHelper;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.scroll.ScrollEvent;
import com.facebook.react.views.view.ReactClippingViewGroup;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.views.view.ReactClippingViewGroupHelper;
import com.facebook.react.uimanager.UIManagerModule;



/**
 * 有弹性的ScrollView
 * 实现下拉弹回和上拉弹回
 * @author zhangjg
 * @date Feb 13, 2014 6:11:33 PM
 */

public class ReboundScrollView extends ReactScrollView implements ReactClippingViewGroup{

	private static final String TAG = "ReboundScrollView";

	//移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
	//目的是达到一个延迟的效果
	private static final float MOVE_FACTOR = 0.5f;

	//松开手指后, 界面回到正常位置需要的动画时间
	private static final int ANIM_TIME = 300;

	//ScrollView的子View， 也是ScrollView的唯一一个子View
	private View contentView;

	//手指按下时的Y值, 用于在移动时计算移动距离
	//如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
	private float startY;

	//用于记录正常的布局位置
	private Rect originalRect = new Rect();

	//手指按下时记录是否可以继续下拉
	private boolean canPullDown = false;

	//手指按下时记录是否可以继续上拉
	private boolean canPullUp = false;

	//在手指滑动的过程中记录是否移动了布局
	private boolean isMoved = false;

	private boolean mRemoveClippedSubviews;
	private @Nullable Rect mClippingRect;
    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();

	public ReboundScrollView(Context context) {
		super(context);
    }

	public ReboundScrollView(Context context, AttributeSet attrs) {
		super(context);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

        contentView = getChildAt(0);
		if(contentView == null) return;

		//ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
		originalRect.set(contentView.getLeft(), contentView.getTop(), contentView
				.getRight(), contentView.getBottom());
	}

	/**
	 * 在该方法中获取ScrollView中的唯一子控件的位置信息
	 * 这个位置信息在整个控件的生命周期中保持不变
	 */

	/**
	 * 在触摸事件中, 处理上拉和下拉的逻辑
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
        contentView = getChildAt(0);
		if (contentView == null) {
			return super.dispatchTouchEvent(ev);
		}

		int action = ev.getAction();

		switch (action) {
			case MotionEvent.ACTION_DOWN:

				//判断是否可以上拉和下拉
				canPullDown = isCanPullDown();
				canPullUp = isCanPullUp();

				//记录按下时的Y值
				startY = ev.getY();
				break;

			case MotionEvent.ACTION_UP:

				if(!isMoved) break;  //如果没有移动布局， 则跳过执行

				// 开启动画
				TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(),
						originalRect.top);
				anim.setDuration(ANIM_TIME);

				contentView.startAnimation(anim);

				// 设置回到正常的布局位置
				contentView.layout(originalRect.left, originalRect.top,
						originalRect.right, originalRect.bottom);

				//将标志位设回false
				canPullDown = false;
				canPullUp = false;
				isMoved = false;

				break;
			case MotionEvent.ACTION_MOVE:

				//在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
				if(!canPullDown && !canPullUp) {
					startY = ev.getY();
					canPullDown = isCanPullDown();
					canPullUp = isCanPullUp();

					break;
				}

				//计算手指移动的距离
				float nowY = ev.getY();
				int deltaY = (int) (nowY - startY);

				//是否应该移动布局
				boolean shouldMove =
						(canPullDown && deltaY > 0)    //可以下拉， 并且手指向下移动
								|| (canPullUp && deltaY< 0)    //可以上拉， 并且手指向上移动
								|| (canPullUp && canPullDown); //既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）

				if(shouldMove){

					//计算偏移量
					int offset = (int)(deltaY * MOVE_FACTOR);
					//随着手指的移动而移动布局
                    this.onScrollChanged(0,-offset,0,offset);
					contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right, originalRect.bottom + offset);
                    //contentView.offsetTopAndBottom(offset);
                 //   ev.offsetLocation(0,-offset);
                    emitScrollEvent(this, 0, -offset);
					isMoved = true;  //记录移动了布局
				}
				break;
			default:
				break;
		}

		return super.dispatchTouchEvent(ev);
	}
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpecAssertions.assertExplicitMeasureSpec(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(
				MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
    }

	/**
	 * 判断是否滚动到顶部
	 */
	private boolean isCanPullDown() {
		return getScrollY() == 0 ||
				contentView.getHeight() < getHeight() + getScrollY();
	}

	/**
	 * 判断是否滚动到底部
	 */
	private boolean isCanPullUp() {
        contentView = getChildAt(0);
		return  contentView.getHeight() <= getHeight() + getScrollY();
	}

	@Override
	public void setRemoveClippedSubviews(boolean removeClippedSubviews) {
		if (removeClippedSubviews && mClippingRect == null) {
			mClippingRect = new Rect();
		}
		mRemoveClippedSubviews = removeClippedSubviews;
		updateClippingRect();
	}

	@Override
	public boolean getRemoveClippedSubviews() {
		return mRemoveClippedSubviews;
	}

	@Override
	public void updateClippingRect() {
		if (!mRemoveClippedSubviews) {
			return;
		}

		Assertions.assertNotNull(mClippingRect);

		ReactClippingViewGroupHelper.calculateClippingRect(this, mClippingRect);
		View contentView = getChildAt(0);
		if (contentView instanceof ReactClippingViewGroup) {
			((ReactClippingViewGroup) contentView).updateClippingRect();
		}
	}

	@Override
	public void getClippingRect(Rect outClippingRect) {
		outClippingRect.set(Assertions.assertNotNull(mClippingRect));
	}
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if(this.mOnScrollDispatchHelper.onScrollChanged(x, y)) {
            if(this.mRemoveClippedSubviews) {
                this.updateClippingRect();
            }
            emitScrollEvent(this, x, y);
        }

    }

    private void emitScrollEvent(ViewGroup scrollView, int scrollX, int scrollY) {
        View contentView = scrollView.getChildAt(0);
        ReactContext reactContext = (ReactContext)scrollView.getContext();
        ((UIManagerModule)reactContext.getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(new ScrollEvent(scrollView.getId(), SystemClock.uptimeMillis(), scrollX, scrollY, contentView.getWidth(), contentView.getHeight(), scrollView.getWidth(), scrollView.getHeight()));
    }
}

